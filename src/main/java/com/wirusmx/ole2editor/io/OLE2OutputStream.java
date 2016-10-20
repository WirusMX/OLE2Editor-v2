package com.wirusmx.ole2editor.io;

import java.io.*;

import static com.wirusmx.ole2editor.Constants.*;

/**
 * Class provides methods for ole2 files writing.
 * Methods write bytes to the temporary file. When method <code>close()</code> is called
 * firstly writes header data abd then temporary file bytes copies to the destination file.
 *
 * @author WirusMX
 */
public class OLE2OutputStream extends OutputStream implements Closeable {
    private final String fileName;

    private OutputStream tempFileOutputStream;
    private File tempFile;

    private ByteArrayOutputStream fileStream;

    private final int sectorSize;
    private final int shortSectorSize;
    private final int standardSectorMinimumSize;


    /**
     * Creates ole2 file output stream to file "<code>fileName</code>" with the default parameters:
     * <code>sectorSize = 512</code>, <code>shortSectorSize = 64</code>,
     * <code>standardSectorMinimumSize = 4096</code>
     *
     * @param fileName - name of target file
     * @throws IOException if an I/O error occurs
     */
    public OLE2OutputStream(String fileName) throws IOException {
        this(fileName, DEFAULT_SECTOR_SIZE, DEFAULT_SHORT_SECTOR_SIZE, STANDARD_SECTOR_MINIMUM_SIZE);
    }

    /**
     * Creates ole2 file output stream to file "<code>fileName</code>" with a specific parameters
     *
     * @param fileName                  - name of target file
     * @param sectorSize                - size of standard sector. Must be a power of 2
     * @param shortSectorSize           - size of short sector. Must be a power of 2
     * @param standardSectorMinimumSize - size of the standard sector minimum size
     * @throws IOException if an I/O error occurs
     */
    public OLE2OutputStream(String fileName, int sectorSize, int shortSectorSize,
                            int standardSectorMinimumSize) throws IOException {
        this.fileName = fileName;
        this.sectorSize = sectorSize;
        this.shortSectorSize = shortSectorSize;
        this.standardSectorMinimumSize = standardSectorMinimumSize;

        tempFile = File.createTempFile("", ".tmp", new File(fileName));
        tempFile.deleteOnExit();
        tempFileOutputStream = new FileOutputStream(tempFile);
    }

    @Override
    public void write(int b) throws IOException {
        fileStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        tempFileOutputStream.flush();
    }

    @Override
    public void close() throws IOException {
        tempFileOutputStream.close();

        FileOutputStream out = new FileOutputStream(fileName);
        writeHeader(out);

        FileInputStream in = new FileInputStream(tempFile);
        copyBytes(in, out);

        in.close();
        out.close();
    }

    public void writeEntry(OLE2Entry entry) {

    }

    /**
     * Copies bytes from input stream to output stream
     *
     * @param in  - input stream
     * @param out - output stream
     * @throws IOException if an I/O error occurs
     */
    private void copyBytes(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[128 * 1024];
        int size = in.available();
        while (size > 0) {
            int count = in.read(buffer);
            size -= count;
            out.write(buffer, 0, count);
        }
    }

    /**
     * Writes header bytes to output stream
     *
     * @param out - output stream
     * @throws IOException if an I/O error occurs
     */
    private void writeHeader(OutputStream out) throws IOException {
        // OLE2 file signature
        out.write(new byte[]{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1,
                (byte) 0xB1, (byte) 0x1A, (byte) 0xE1});

        // OLE2 file UID
        out.write(new byte[16]);

        // OLE2 file version
        out.write(new byte[]{(byte) 0x3E, (byte) 0x00, (byte) 0x03, (byte) 0x00});

        //OLE2 byte order
        out.write(new byte[]{(byte) 0xFE, (byte) 0xFF});

        //OLE2 sector size
        //TODO

        //OLE2 short sector size
        //TODO

        // Empty bytes
        out.write(new byte[10]);

    }
}
