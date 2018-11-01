package com.mcxtzhang.redbookeditphoto.base;

import java.io.Serializable;

public class UGCPicTagDo implements Serializable {
    /**
    * 标签id
    */
    private String tagReferId;

    /**
    * 标签类型
    */
    private int tagType;

    /**
    * 标签y轴的归一化坐标
    */
    private double yPosition;

    /**
    * 标签x轴的归一化坐标
    */
    private double xPosition;

    /**
    * 标签内容
    */
    private String content;

    /**
    * 标签icon链接
    */
    private String tagIconUrl;

    /**
    * 标签点击跳转url
    */
    private String tagJumpUrl;

    /**
    * 标签尖角朝向(1:向左，2:向右)
    */
    private int direction;

    public String getTagReferId() {
        return tagReferId;
    }

    public void setTagReferId(String tagReferId) {
        this.tagReferId = tagReferId;
    }

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public double getYPosition() {
        return yPosition;
    }

    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public double getXPosition() {
        return xPosition;
    }

    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTagIconUrl() {
        return tagIconUrl;
    }

    public void setTagIconUrl(String tagIconUrl) {
        this.tagIconUrl = tagIconUrl;
    }

    public String getTagJumpUrl() {
        return tagJumpUrl;
    }

    public void setTagJumpUrl(String tagJumpUrl) {
        this.tagJumpUrl = tagJumpUrl;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}