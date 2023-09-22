package com.example.eight.artwork.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelicApiResponseObject {
    private String id;

    @JsonProperty("imgUri")
    private String imgUri;

    @JsonProperty("name")
    private String name;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("sizeInfo")
    private String sizeInfo;

    @JsonProperty("materialName1")
    private String materialName1;

    @JsonProperty("nationalityName1")
    private String nationalityName1;

    @JsonProperty("sizeRangeName")
    private String sizeRangeName;

    @JsonProperty("purposeName1")
    private String purposeName1;

    @JsonProperty("purposeName2")
    private String purposeName2;

    @JsonProperty("purposeName3")
    private String purposeName3;

    @JsonProperty("museumName1")
    private String museumName1;

    @JsonProperty("museumName2")
    private String museumName2;

    @JsonProperty("museumName3")
    private String museumName3;

    @JsonProperty("relicNo")
    private String relicNo;

    @JsonProperty("indexWord")
    private String indexWord;

    @JsonProperty("nationalityCode")
    private String nationalityCode;

    @JsonProperty("nationalityCode1")
    private String nationalityCode1;

    @JsonProperty("nationalityCode2")
    private String nationalityCode2;

    @JsonProperty("materialCode")
    private String materialCode;

    @JsonProperty("materialCode1")
    private String materialCode1;

    @JsonProperty("purposeCode")
    private String purposeCode;

    @JsonProperty("purposeCode1")
    private String purposeCode1;

    @JsonProperty("purposeCode2")
    private String purposeCode2;

    @JsonProperty("purposeCode3")
    private String purposeCode3;

    @JsonProperty("museumCode")
    private String museumCode;

    @JsonProperty("museumCode1")
    private String museumCode1;

    @JsonProperty("museumCode2")
    private String museumCode2;

    @JsonProperty("museumCode3")
    private String museumCode3;

    @JsonProperty("imgThumUriL")
    private String imgThumUriL;

    @JsonProperty("imgThumUriM")
    private String imgThumUriM;

    @JsonProperty("imgThumUriS")
    private String imgThumUriS;

    // Getter와 Setter 메서드 추가

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSizeInfo() {
        return sizeInfo;
    }

    public void setSizeInfo(String sizeInfo) {
        this.sizeInfo = sizeInfo;
    }

    public String getMaterialName1() {
        return materialName1;
    }

    public void setMaterialName1(String materialName1) {
        this.materialName1 = materialName1;
    }

    public String getNationalityName1() {
        return nationalityName1;
    }

    public void setNationalityName1(String nationalityName1) {
        this.nationalityName1 = nationalityName1;
    }

    public String getSizeRangeName() {
        return sizeRangeName;
    }

    public void setSizeRangeName(String sizeRangeName) {
        this.sizeRangeName = sizeRangeName;
    }

    public String getPurposeName1() {
        return purposeName1;
    }

    public void setPurposeName1(String purposeName1) {
        this.purposeName1 = purposeName1;
    }

    public String getPurposeName2() {
        return purposeName2;
    }

    public void setPurposeName2(String purposeName2) {
        this.purposeName2 = purposeName2;
    }

    public String getPurposeName3() {
        return purposeName3;
    }

    public void setPurposeName3(String purposeName3) {
        this.purposeName3 = purposeName3;
    }

    public String getMuseumName1() {
        return museumName1;
    }

    public void setMuseumName1(String museumName1) {
        this.museumName1 = museumName1;
    }

    public String getMuseumName2() {
        return museumName2;
    }

    public void setMuseumName2(String museumName2) {
        this.museumName2 = museumName2;
    }

    public String getMuseumName3() {
        return museumName3;
    }

    public void setMuseumName3(String museumName3) {
        this.museumName3 = museumName3;
    }

    public String getRelicNo() {
        return relicNo;
    }

    public void setRelicNo(String relicNo) {
        this.relicNo = relicNo;
    }

    public String getIndexWord() {
        return indexWord;
    }

    public void setIndexWord(String indexWord) {
        this.indexWord = indexWord;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getNationalityCode1() {
        return nationalityCode1;
    }

    public void setNationalityCode1(String nationalityCode1) {
        this.nationalityCode1 = nationalityCode1;
    }

    public String getNationalityCode2() {
        return nationalityCode2;
    }

    public void setNationalityCode2(String nationalityCode2) {
        this.nationalityCode2 = nationalityCode2;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialCode1() {
        return materialCode1;
    }

    public void setMaterialCode1(String materialCode1) {
        this.materialCode1 = materialCode1;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getPurposeCode1() {
        return purposeCode1;
    }

    public void setPurposeCode1(String purposeCode1) {
        this.purposeCode1 = purposeCode1;
    }

    public String getPurposeCode2() {
        return purposeCode2;
    }

    public void setPurposeCode2(String purposeCode2) {
        this.purposeCode2 = purposeCode2;
    }

    public String getPurposeCode3() {
        return purposeCode3;
    }

    public void setPurposeCode3(String purposeCode3) {
        this.purposeCode3 = purposeCode3;
    }

    public String getMuseumCode() {
        return museumCode;
    }

    public void setMuseumCode(String museumCode) {
        this.museumCode = museumCode;
    }

    public String getMuseumCode1() {
        return museumCode1;
    }

    public void setMuseumCode1(String museumCode1) {
        this.museumCode1 = museumCode1;
    }

    public String getMuseumCode2() {
        return museumCode2;
    }

    public void setMuseumCode2(String museumCode2) {
        this.museumCode2 = museumCode2;
    }

    public String getMuseumCode3() {
        return museumCode3;
    }

    public void setMuseumCode3(String museumCode3) {
        this.museumCode3 = museumCode3;
    }

    public String getImgThumUriL() {
        return imgThumUriL;
    }

    public void setImgThumUriL(String imgThumUriL) {
        this.imgThumUriL = imgThumUriL;
    }

    public String getImgThumUriM() {
        return imgThumUriM;
    }

    public void setImgThumUriM(String imgThumUriM) {
        this.imgThumUriM = imgThumUriM;
    }

    public String getImgThumUriS() {
        return imgThumUriS;
    }

    public void setImgThumUriS(String imgThumUriS) {
        this.imgThumUriS = imgThumUriS;
    }

}
