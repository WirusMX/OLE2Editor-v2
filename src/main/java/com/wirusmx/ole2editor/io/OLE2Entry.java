package com.wirusmx.ole2editor.io;

import com.wirusmx.ole2editor.utils.Converter;

import java.nio.ByteOrder;
import java.util.Date;

import static com.wirusmx.ole2editor.Constants.ENTRY_HEADER_SIZE;

/**
 * Type which describes the ole2 entry
 *
 * @author WirusMX
 */
public class OLE2Entry {
    private byte[] name;
    private int nameLength;
    private EntryType type;
    private EntryColor nodeColor;
    private int leftChildID;
    private int rightChildID;
    private int rootNodeID;
    private byte[] uniqueID;
    private byte[] userFlags;
    private Date creationTimeStamp;
    private Date modificationTimeStamp;
    private int firstStreamSectorID;
    private int size;
    private byte[] endBytes;
    private ByteOrder byteOrder;

    /**
     * Construct a new ole2 file entry from raw bytes. Count of available bytes must be 128
     *
     * @param rawData   - raw bytes from the file stream
     * @param offset    - offset into bytes array
     * @param byteOrder - byte order
     */
    public OLE2Entry(byte[] rawData, int offset, ByteOrder byteOrder) {
        if (rawData.length - offset < ENTRY_HEADER_SIZE) {
            throw new IllegalArgumentException("Bytes array must contains at list " + ENTRY_HEADER_SIZE + " elements");
        }

        name = new byte[64];

        System.arraycopy(rawData, offset, name, 0, 64);
        nameLength = Converter.bytesToInt16(byteOrder, rawData[offset + 64], rawData[offset + 65]);

        type = EntryType.values()[rawData[offset + 66]];

        nodeColor = EntryColor.values()[rawData[offset + 67]];

        leftChildID = Converter.bytesToInt32(byteOrder, rawData[offset + 68], rawData[offset + 69],
                rawData[offset + 70], rawData[offset + 71]);
        rightChildID = Converter.bytesToInt32(byteOrder, rawData[offset + 72], rawData[offset + 73],
                rawData[offset + 74], rawData[offset + 75]);
        rootNodeID = Converter.bytesToInt32(byteOrder, rawData[offset + 76], rawData[offset + 77],
                rawData[offset + 78], rawData[offset + 79]);

        uniqueID = new byte[16];
        System.arraycopy(rawData, offset + 80, uniqueID, 0, 16);

        userFlags = new byte[]{rawData[offset + 96], rawData[offset + 97], rawData[offset + 98], rawData[offset + 99]};

        byte[] temp = new byte[8];
        System.arraycopy(rawData, offset + 100, temp, 0, 8);
        creationTimeStamp = new Date(Converter.bytesToLong(byteOrder, temp));

        System.arraycopy(rawData, offset + 108, temp, 0, 8);
        modificationTimeStamp = new Date(Converter.bytesToLong(byteOrder, temp));

        firstStreamSectorID = Converter.bytesToInt32(byteOrder, rawData[offset + 116], rawData[offset + 117],
                rawData[offset + 118], rawData[offset + 119]);

        size = Converter.bytesToInt32(byteOrder, rawData[offset + 120], rawData[offset + 121],
                rawData[offset + 122], rawData[offset + 123]);

        endBytes = new byte[]{rawData[offset + 124], rawData[offset + 125], rawData[offset + 126], rawData[offset + 127]};
    }

    /**
     * Construct a new ole2 file entry from raw bytes
     *
     * @param rawData   - raw bytes from the file stream
     * @param byteOrder - byte order
     */
    public OLE2Entry(byte[] rawData, ByteOrder byteOrder) {
        this(rawData, 0, byteOrder);
    }

    /**
     * Construct a new ole2 file entry
     *
     * @param name                  - Bytes array of the name of the entry, always 16-bit Unicode characters, with
     *                              trailing zero character (results in a maximum name length of 31 characters)
     * @param nameLength            - Size of the used area of the bytes buffer of the name (not character count),
     *                              including the trailing zero character
     * @param type                  - Type of the entry: 0x00 = Empty, 0x01 = User storage, 0x02 = User stream,
     *                              0x03 = LockBytes, 0x04 = Property, 0x05 = Root storage
     * @param nodeColor             - Node colour of the entry inside red-black tree: 0x00 = Red, 0x01 = Black
     * @param leftChildID           - ID of the left child node inside the red-black tree of all direct members
     *                              of the parent storage, –1 if there is no left child
     * @param rightChildID          - ID of the right child node inside the red-black tree of all direct members
     *                              of the parent storage, –1 if there is no right child
     * @param rootNodeID            - ID of the root node entry of the red-black tree of all storage members, –1 otherwise
     * @param uniqueID              - Unique identifier, if this is a storage. Array of 16 bytes.
     * @param userFlags             - User flags Array of 4 bytes
     * @param creationTimeStamp     - Time stamp of creation of this entry
     * @param modificationTimeStamp - Time stamp of last modification of this entry
     * @param firstStreamSectorID   - ID of first sector in the stream
     * @param size                  - Total stream size in bytes
     * @param endBytes              - last 4 bytes of node
     * @param byteOrder             - byte order
     */
    public OLE2Entry(byte[] name, int nameLength, EntryType type, EntryColor nodeColor, int leftChildID,
                     int rightChildID, int rootNodeID, byte[] uniqueID, byte[] userFlags, Date creationTimeStamp,
                     Date modificationTimeStamp, int firstStreamSectorID, int size, byte[] endBytes, ByteOrder byteOrder) {
        if (name.length > 64) {
            throw new IllegalArgumentException("Bytes array of the entry name must contains not more 64 elements");
        }

        if (nameLength > 64) {
            throw new IllegalArgumentException("Bytes array of the entry name must contains not more 64 elements");
        }

        this.name = name;
        this.nameLength = nameLength;
        this.type = type;
        this.nodeColor = nodeColor;
        this.leftChildID = leftChildID;
        this.rightChildID = rightChildID;
        this.rootNodeID = rootNodeID;
        this.uniqueID = uniqueID;
        this.userFlags = userFlags;
        this.creationTimeStamp = creationTimeStamp;
        this.modificationTimeStamp = modificationTimeStamp;
        this.firstStreamSectorID = firstStreamSectorID;
        this.size = size;
        this.endBytes = endBytes;
        this.byteOrder = byteOrder;
    }

    /**
     * @return bytes array of the name of the entry, always 16-bit Unicode characters, with trailing zero character
     */
    public byte[] getName() {
        return name;
    }

    /**
     * @return entry name as string
     */
    public String getNameAsString() {
        if (nameLength == 0) {
            return "";
        }
        return Converter.utf16BytesToString(byteOrder, name);
    }

    /**
     * @return size of the used area of the bytes buffer of the name (not character count), including the trailing
     * zero character
     */
    public int getNameLength() {
        return nameLength;
    }

    /**
     * @return type of the entry
     */
    public EntryType getType() {
        return type;
    }

    /**
     * @return node colour of the entry inside red-black tree
     */
    public EntryColor getNodeColor() {
        return nodeColor;
    }

    /**
     * @return ID of the left child node inside the red-black tree of all direct members of the parent storage,
     * –1 if there is no left child
     */
    public int getLeftChildID() {
        return leftChildID;
    }

    /**
     * @return ID of the right child node inside the red-black tree of all direct members of the parent storage,
     * –1 if there is no right child
     */
    public int getRightChildID() {
        return rightChildID;
    }

    /**
     * @return ID of the root node entry of the red-black tree of all storage members, –1 otherwise
     */
    public int getRootNodeID() {
        return rootNodeID;
    }

    /**
     * @return unique identifier, if this is a storage.
     */
    public byte[] getUniqueID() {
        return uniqueID;
    }

    /**
     * @return Unique identifier, if this is a storage.
     */
    public byte[] getUserFlags() {
        return userFlags;
    }

    /**
     * @return time stamp of creation of this entry.  Most implementations do not write a valid time stamp!
     */
    public Date getCreationTimeStamp() {
        return creationTimeStamp;
    }

    /**
     * @return time stamp of last modification of this entry. Most implementations do not write a valid time stamp!
     */
    public Date getModificationTimeStamp() {
        return modificationTimeStamp;
    }

    /**
     * @return ID of first sector in the stream, or -1 if entry is a storage
     */
    public int getFirstStreamSectorID() {
        return firstStreamSectorID;
    }

    /**
     * @return total stream size in bytes, total size of the shortstream container stream, if this is
     * the root storage entry, 0 otherwise
     */
    public int getSize() {
        return size;
    }

    /**
     * @return byte order
     */
    public ByteOrder getByteOrder() {
        return byteOrder;
    }

    /**
     * @return last 4 bytes of node. Not used. Must be 0.
     */
    public byte[] getEndBytes() {
        return endBytes;
    }

    @Override
    public String toString() {
        switch (type) {
            case USER_STORAGE:
            case ROOT_STORAGE:
                return "[" + getNameAsString() + "]";
            default:
                return getNameAsString();
        }
    }

    /**
     * Enum describes the types of entries in ole2 file. Do not mix the order of values!
     */
    public enum EntryType {
        EMPTY,
        USER_STORAGE,
        USER_STREAM,
        LOCK_BYTES,
        PROPERTY,
        ROOT_STORAGE
    }

    /**
     * Enum describes the colors of node in red-black tree. Do not mix the order of values!
     */
    public enum EntryColor {
        RED,
        BLACK
    }
}
