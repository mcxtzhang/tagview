package com.mcxtzhang.redbookeditphoto.base;

public class UGCPicTag {
	/*
	  name:UGCPicTag_TagReferId
	  type:string
	  标签id
	*/
	public String tagReferId;
	/*
	  name:UGCPicTag_TagType
	  type:int
	  标签类型
	*/
	public int tagType;
	/*
	  name:UGCPicTag_YPosition
	  type:int
	  标签y轴的归一化坐标
	*/
	public double yPosition;
	/*
	  name:UGCPicTag_XPosition
	  type:int
	  标签x轴的归一化坐标
	*/
	public double xPosition;
	/*
	  name:UGCPicTag_Content
	  type:string
	  标签内容
	*/
	public String content;
	/*
	  name:UGCPicTag_TagIconUrl
	  type:string
	  标签icon链接
	*/
	public String tagIconUrl;
	/*
	  name:UGCPicTag_TagJumpUrl
	  type:string
	  标签点击跳转url
	*/
	public String tagJumpUrl;
	/*
	  name:UGCPicTag_Direction
	  type:int
	  标签尖角朝向(1:向左，2:向右)
	*/
	public int direction;


	public UGCPicTag() {
		int _babelInitDepth = 0;
		direction = 0;
		tagJumpUrl = "";
		tagIconUrl = "";
		content = "";
		xPosition = 0;
		yPosition = 0;
		tagType = 0;
		tagReferId = "";
	}

}
