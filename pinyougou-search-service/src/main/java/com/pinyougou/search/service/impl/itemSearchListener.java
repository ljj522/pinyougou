package com.pinyougou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;

@Component
public class itemSearchListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage)message;
        try {
            String text = textMessage.getText();//JSON字符串
            System.out.println("监听接收到消息..."+text);

            List<TbItem>itemList = JSON.parseArray(text,TbItem.class);
            itemSearchService.imporList(itemList);
            System.out.println("成功导入到索引库");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
