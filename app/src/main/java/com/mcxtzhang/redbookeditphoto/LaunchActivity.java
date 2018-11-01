package com.mcxtzhang.redbookeditphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mcxtzhang.redbookeditphoto.base.UGCPicTag;
import com.mcxtzhang.redbookeditphoto.show.ViewTagActivity;

import java.util.LinkedList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {
    public static final List<UGCPicTag> TAG_DATA_LIST = new LinkedList<>();

    static {
        UGCPicTag tag1 = new UGCPicTag();
        tag1.content = ("电影啊电影");
        tag1.direction = (1);
        tag1.tagJumpUrl = ("dianping://dolphinconfig");
        tag1.tagIconUrl = ("http://pic31.photophoto.cn/20140607/0020033078299442_b.jpg");
        tag1.xPosition = (0.1);
        tag1.yPosition = (0.01);
        TAG_DATA_LIST.add(tag1);
        UGCPicTag tag2 = new UGCPicTag();
        tag2.content = ("一");
        tag2.direction = (2);
        tag2.tagJumpUrl = ("dianping://dolphinconfig");
        tag2.tagIconUrl = ("http://pic31.photophoto.cn/20140607/0020033078299442_b.jpg");
        tag2.xPosition = (0.3);
        tag2.yPosition = (0.3);
        TAG_DATA_LIST.add(tag2);
        UGCPicTag tag3 = new UGCPicTag();
        tag3.content = ("地点啊地点六我是很多个字啊");
        tag3.direction = (1);
        tag3.tagJumpUrl = ("dianping://dolphinconfig");
        tag3.tagIconUrl = ("http://pic31.photophoto.cn/20140607/0020033078299442_b.jpg");
        tag3.xPosition = (0.5);
        tag3.yPosition = (0.5);
        TAG_DATA_LIST.add(tag3);

        UGCPicTag tag4 = new UGCPicTag();
        tag4.content = ("地点啊地点六我是很多个字啊");
        tag4.direction = (2);
        tag4.tagJumpUrl = ("dianping://dolphinconfig");
        tag4.tagIconUrl = ("http://pic31.photophoto.cn/20140607/0020033078299442_b.jpg");
        tag4.xPosition = (0.7);
        tag4.yPosition = (0.7);
        TAG_DATA_LIST.add(tag4);

        UGCPicTag tag5 = new UGCPicTag();
        tag5.content = ("地点啊地点六我是很多个字啊");
        tag5.direction = (2);
        tag5.tagJumpUrl = ("dianping://dolphinconfig");
        tag5.tagIconUrl = ("http://pic31.photophoto.cn/20140607/0020033078299442_b.jpg");
        tag5.xPosition = (0.9);
        tag5.yPosition = (0.9);
        TAG_DATA_LIST.add(tag5);

        UGCPicTag tag6 = new UGCPicTag();
        tag6.content = ("地点啊地点六我是很多个字啊");
        tag6.direction = (2);
        tag6.tagJumpUrl = ("dianping://dolphinconfig");
        tag6.tagIconUrl = ("http://pic31.photophoto.cn/20140607/0020033078299442_b.jpg");
        tag6.xPosition = (0.95);
        tag6.yPosition = (0.95);
        //测试右边界
        TAG_DATA_LIST.add(tag6);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        findViewById(R.id.btnJumpEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.btnJumpView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LaunchActivity.this, ViewTagActivity.class));
            }
        });
    }
}
