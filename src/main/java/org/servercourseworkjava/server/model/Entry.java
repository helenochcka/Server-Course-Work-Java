package org.servercourseworkjava.server.model;

public class Entry {
    private final char SUBINTERNAL_SEPARATOR = (char) 59;
    private final char INTERNAL_SEPARATOR = (char) 47;
    private String id;
    private String proofOfOwnership;
    private String owner;
    private String realEstateType;
    private String location;
    private int squareFootage;
    private String dateOfPurchase;

    public Entry() {

    }

    public Entry(String id, String proofOfOwnership, String owner, String realEstateType,
                 String location, int squareFootage, String dateOfPurchase) {
        this.id = id;
        this.proofOfOwnership = proofOfOwnership;
        this.owner = owner;
        this.realEstateType = realEstateType;
        this.location = location;
        this.squareFootage = squareFootage;
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getId() {
        return id;
    }

    public String getProofOfOwnership() {
        return proofOfOwnership;
    }

    public String getOwner() {
        return owner;
    }

    public String getRealEstateType() {
        return realEstateType;
    }

    public String getLocation() {
        return location;
    }

    public int getSquareFootage() {
        return squareFootage;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProofOfOwnership(String proofOfOwnership) {
        this.proofOfOwnership = proofOfOwnership;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setRealEstateType(String realEstateType) {
        this.realEstateType = realEstateType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String pack() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(INTERNAL_SEPARATOR)
                .append(id)
                .append(SUBINTERNAL_SEPARATOR)
                .append(proofOfOwnership)
                .append(SUBINTERNAL_SEPARATOR)
                .append(owner)
                .append(SUBINTERNAL_SEPARATOR)
                .append(realEstateType)
                .append(SUBINTERNAL_SEPARATOR)
                .append(location)
                .append(SUBINTERNAL_SEPARATOR)
                .append(squareFootage)
                .append(SUBINTERNAL_SEPARATOR)
                .append(dateOfPurchase);
        return stringBuilder.toString();
    }

    public static Entry parseEntry(String string, Character fieldSeparator) {
        String[] stringTokens = string.split(String.valueOf(fieldSeparator));
        Entry entry = new Entry();

        try {
            entry.setId(stringTokens[0]);
            entry.setProofOfOwnership(stringTokens[1]);
            entry.setOwner(stringTokens[2]);
            entry.setRealEstateType(stringTokens[3]);
            entry.setLocation(stringTokens[4]);
            entry.setSquareFootage(Integer.parseInt(stringTokens[5]));
            entry.setDateOfPurchase(stringTokens[6]);
        } catch (NumberFormatException e) {}

        return entry;
    }

    @Override
    public String toString() {
        return "(" + id + " " +
                proofOfOwnership + " " +
                owner + " " +
                realEstateType + " " +
                location + " " +
                squareFootage + " " +
                dateOfPurchase + ")";
    }
}
