package com.wirusmx.ole2editor.io;

import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.exceptions.SIDOutOfBoundsException;

import java.io.*;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import static com.wirusmx.ole2editor.Constants.ENTRY_HEADER_SIZE;
import static com.wirusmx.ole2editor.Constants.FILE_HEADER_SIZE;
import static com.wirusmx.ole2editor.utils.Converter.*;

/**
 * Class provides methods for ole2 files reading
 *
 * @author WirusMX
 */
public class OLE2InputStream extends InputStream implements Closeable {
    private final String fileName;
    private InputStream fileInputStream;

    private ByteArrayInputStream fileStream;
    private int currentEntryID;
    private OLE2Entry currentEntry;

    private final ByteOrder byteOrder;

    private final int sectorSize;
    private final int shortSectorSize;
    private final int rootEntrySID;
    private final int standardSectorMinimumSize;

    private final List<Integer> masterSectorAllocationTable;
    private final List<Integer> sectorAllocationTable;
    private final List<Integer> shortSectorAllocationTable;

    private boolean readAllStreamSectorsIgnoreSize = false;

    /**
     * Construct a new input bytes stream from the ole2File file
     *
     * @param ole2FileName name of file for reading bytes
     * @throws FileNotFoundException if file not found
     */
    public OLE2InputStream(String ole2FileName) throws IOException, IllegalFileStructure {
        fileName = ole2FileName;
        resetInputStream();
        currentEntryID = -1;

        byte[] buffer = readHeader();

        int t = bytesToShort(ByteOrder.LITTLE_ENDIAN, buffer[28], buffer[29]);
        switch (t) {
            case -1: {
                byteOrder = ByteOrder.BIG_ENDIAN;
                break;
            }
            case -2: {
                byteOrder = ByteOrder.LITTLE_ENDIAN;
                break;
            }
            default: {
                throw new IllegalFileStructure();
            }
        }

        sectorSize = (int) Math.pow(2, bytesToShort(byteOrder, buffer[30], buffer[31]));
        shortSectorSize = (int) Math.pow(2, bytesToShort(byteOrder, buffer[32], buffer[33]));
        rootEntrySID = bytesToInt(byteOrder, buffer[48], buffer[49], buffer[50], buffer[51]);
        standardSectorMinimumSize = bytesToInt(byteOrder, buffer[56], buffer[57], buffer[58], buffer[59]);

        int SATSectorsCount = bytesToInt(byteOrder, buffer[44], buffer[45], buffer[46], buffer[47]);

        int nextSSATSID = bytesToInt(byteOrder, buffer[60], buffer[61], buffer[62], buffer[63]);
        int SSATSectorsCount = bytesToInt(byteOrder, buffer[64], buffer[65], buffer[66], buffer[67]);

        masterSectorAllocationTable = new ArrayList<>();
        int offset = 76;
        for (; offset < FILE_HEADER_SIZE && SATSectorsCount > 0; offset += 4) {
            masterSectorAllocationTable.add(bytesToInt(byteOrder, buffer[offset], buffer[offset + 1],
                    buffer[offset + 2], buffer[offset + 3]));
            SATSectorsCount--;
        }
        int nextMSATSID = bytesToInt(byteOrder, buffer[68], buffer[69], buffer[70], buffer[71]);
        while (nextMSATSID >= 0 && SATSectorsCount > 0) {
            buffer = readSector(nextMSATSID);

            for (offset = 0; offset < sectorSize - 4 && SATSectorsCount > 0; offset += 4) {
                masterSectorAllocationTable.add(bytesToInt(byteOrder, buffer[offset], buffer[offset + 1],
                        buffer[offset + 2], buffer[offset + 3]));
                SATSectorsCount--;
            }
            nextMSATSID = bytesToInt(byteOrder, buffer[offset], buffer[offset + 1], buffer[offset + 2],
                    buffer[offset + 3]);
        }

        sectorAllocationTable = new ArrayList<>();
        for (Integer sid : masterSectorAllocationTable) {
            if (sid < 0) {
                continue;
            }
            buffer = readSector(sid);
            for (offset = 0; offset < sectorSize; offset += 4) {
                sectorAllocationTable.add(bytesToInt(byteOrder, buffer[offset], buffer[offset + 1],
                        buffer[offset + 2], buffer[offset + 3]));
            }
        }

        shortSectorAllocationTable = new ArrayList<>();
        while (nextSSATSID >= 0 && SSATSectorsCount > 0) {
            buffer = readSector(nextSSATSID);

            for (offset = 0; offset < sectorSize - 4; offset += 4) {
                shortSectorAllocationTable.add(bytesToInt(byteOrder, buffer[offset], buffer[offset + 1],
                        buffer[offset + 2], buffer[offset + 3]));
            }

            nextSSATSID = bytesToInt(byteOrder, buffer[offset], buffer[offset + 1], buffer[offset + 2],
                    buffer[offset + 3]);

            SSATSectorsCount--;
        }
    }

    public boolean readAllStreamSectorsIgnoreSize() {
        return readAllStreamSectorsIgnoreSize;
    }

    public void setReadAllStreamSectorsIgnoreSize(boolean value) {
        readAllStreamSectorsIgnoreSize = value;
    }

    /**
     * Reads the next byte of data from the input stream.
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        return fileStream.read();
    }

    @Override
    public long skip(long n) throws IOException {
        return fileStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return fileStream.available();
    }

    @Override
    public void close() throws IOException {
        if (fileInputStream != null) {
            fileInputStream.close();
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        fileStream.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        currentEntryID = -1;
        fileStream = null;
        resetInputStream();
    }

    @Override
    public boolean markSupported() {
        return fileStream.markSupported();
    }

    /**
     * Read next entry from ole2 file
     *
     * @return next entry from ole2 file if entry exists, or null
     */
    public OLE2Entry readNextEntry() throws IOException {
        currentEntryID++;
        int entriesPerSector = sectorSize / ENTRY_HEADER_SIZE;
        int fullSectors = currentEntryID / entriesPerSector;
        int positionInSector = currentEntryID % entriesPerSector;
        int entrySID = rootEntrySID;
        for (int i = 0; i < fullSectors; i++) {
            entrySID = sectorAllocationTable.get(entrySID);
        }
        byte[] buffer = readSector(entrySID);
        currentEntry = new OLE2Entry(buffer, positionInSector * ENTRY_HEADER_SIZE, byteOrder);
        fileStream = new ByteArrayInputStream(getCurrentStreamBytes());
        return currentEntry;
    }

    /**
     * @return returns <code>true</code> if next entry is present
     */
    public boolean hasNextEntry() {
        int nextEntryID = currentEntryID + 1;
        int entriesPerSector = sectorSize / ENTRY_HEADER_SIZE;
        int fullSectors = nextEntryID / entriesPerSector;
        int entrySID = rootEntrySID;
        for (int i = 0; i < fullSectors; i++) {
            if (entrySID < 0) {
                return false;
            }
            entrySID = sectorAllocationTable.get(entrySID);
        }

        return entrySID >= 0;
    }


    /**
     * Reopens input stream
     *
     * @throws IOException if an I/O error occurs
     */
    private void resetInputStream() throws IOException {
        if (fileInputStream != null) {
            fileInputStream.close();
        }

        fileInputStream = new FileInputStream(fileName);
    }

    /**
     * @return raw data of current stream, or empty byte array if stream is empty or user storage
     */
    private byte[] getCurrentStreamBytes() throws IOException {
        if (currentEntry.getType() == OLE2Entry.EntryType.USER_STORAGE
                || currentEntry.getType() == OLE2Entry.EntryType.EMPTY) {
            return new byte[0];
        }

        if (currentEntry.getSize() < standardSectorMinimumSize) {
            return getShortStreamBytes();
        }

        return getStreamBytes();
    }

    /**
     * @return raw data of current stream if it divides on normal sectors
     */
    private byte[] getStreamBytes() throws IOException {
        int sid = currentEntry.getFirstStreamSectorID();
        int processedBytes = 0;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        while (sid >= 0) {
            byte[] buffer = readSector(sid);
            int count = buffer.length;
            if (readAllStreamSectorsIgnoreSize && currentEntry.getSize() - processedBytes < count) {
                count = currentEntry.getSize() - processedBytes;
            }

            outputStream.write(buffer, 0, count);

            if (count != buffer.length) {
                break;
            }

            processedBytes += count;

            sid = sectorAllocationTable.get(sid);
        }
        return outputStream.toByteArray();
    }

    /**
     * @return raw data of current stream if it divides on short sectors
     */
    private byte[] getShortStreamBytes() {
        //TODO
        return new byte[0];
    }

    /**
     * @return raw header bytes
     * @throws IOException if an I/O error occurs
     */
    private byte[] readHeader() throws IOException {
        return readHeaderOrSector(true, -1);
    }

    /**
     * @param SID - SID of the sector to be read
     * @return raw data of sector with parameter ID
     * @throws IOException if an I/O error occurs
     */
    private byte[] readSector(int SID) throws IOException {
        if (SID < 0) {
            throw new IllegalArgumentException("SID must be positive!");
        }
        return readHeaderOrSector(false, SID);
    }

    /**
     * Reads one sector of ole2file or header.
     *
     * @param readHeader - if parameter is <code>true</code> method ignores parameter <code>SID</code>
     *                   and returns header, else returns sector data by the SID
     * @param SID        - SID of sector which data must be read
     * @return header or sector data
     */
    private byte[] readHeaderOrSector(boolean readHeader, int SID) throws IOException {
        long offset;
        int bufferSize;

        if (readHeader) {
            offset = 0;
            bufferSize = FILE_HEADER_SIZE;
        } else {
            offset = FILE_HEADER_SIZE + SID * sectorSize;
            bufferSize = sectorSize;
        }

        byte[] buffer = new byte[bufferSize];

        resetInputStream();
        long skippedBytes = fileInputStream.skip(offset);
        if (skippedBytes != offset) {
            throw new SIDOutOfBoundsException();
        }

        if (fileInputStream.available() < sectorSize) {
            throw new IllegalArgumentException("File " + fileName + " too short to be ole2 file!");
        }

        int count;
        int processedBytes = 0;
        while (processedBytes < bufferSize) {
            count = fileInputStream.read(buffer, processedBytes, bufferSize - processedBytes);
            processedBytes += count;
        }

        return buffer;
    }
}
