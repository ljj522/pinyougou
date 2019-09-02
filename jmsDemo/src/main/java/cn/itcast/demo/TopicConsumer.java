package cn.itcast.demo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

//消费者

public class TopicConsumer {

    public static void main(String[] arge)throws Exception{
        //1.创建连接工厂
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.3.19:61616");
        //2.获取连接
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4.获取session  (参数1：是否启动事务,参数2：消息确认模式)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建队列对象
        Topic topic = session.createTopic("test-topic");

        //6.创建信息消费者对象
        MessageConsumer consumer = session.createConsumer(topic);
        //7.监听消息
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage textMessage=(TextMessage)message;
                try {
                    System.out.println("接收到消息："+textMessage.getText());
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        //8.等待键盘输入
        System.in.read();

        //9.关闭资源
        consumer.close();
        session.close();
        connection.close();

    }
}
