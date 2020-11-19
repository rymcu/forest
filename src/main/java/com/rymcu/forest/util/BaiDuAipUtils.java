package com.rymcu.forest.util;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.nlp.AipNlp;
import com.rymcu.forest.dto.baidu.TagNlpDTO;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * @author ronger
 */
public class BaiDuAipUtils {

    public static final String APP_ID = "18094949";
    public static final String API_KEY = "3h3BOgejXI1En5aq1iGHeWrF";
    public static final String SECRET_KEY = "8guDNvxWF1wu8ogpVxLHlRY5FeOBE8z7";

    public static final Integer MAX_CONTENT_LENGTH = 3000;

    public static void main(String[] args) {
        String title = "51单片机第4章--跑马灯实验";
        System.out.println(title.length());
        String content = "<h2>4.1 进制转换基础知识</h2>\n" +
                "<p>进制实际是一个非常简单易懂的概念，对于初学者来说也很容易上手。我们接触最多的就是十进制了，它的特点为逢十进一，包含 0，1，2，3，4，5，6，7，8，9 共十个元素。在生活中我们用到的基本都是十进制了，所以大家对它已经非常熟悉并能应用自如，但是在计算机（包括单片机）世界里，所有都是以二进制为基础的。二进制的特点为逢二进一，包含 0,1 共两个元素。计算机中的数据都是以二进制存储的，这就是我们所说的 0，1 世界。通常我们讲的 32 位或 64 位操作系统这里的位指的就是二进制位数。因为我们实际多用十进制，那么我们在和计算机系统沟通过程中，十进制与二进制之间的转换就变得很重要了。进制之间的转换如下表所示。<br />\n" +
                "<img src=\"https://static.rymcu.com/article/1584363905093.png\" alt=\"表4-1进制转换.png\" /><br />\n" +
                "二进制转换十进制公式如下：<br />\n" +
                "<img src=\"https://static.rymcu.com/article/1584363941039.png\" alt=\"进制转换公式.png\" /><br />\n" +
                "其中，n 表示二进制的位数。<br />\n" +
                "下面我们举个例子来更加直观的说明这个公式：<br />\n" +
                "例如：1101，这是一个 4 位的二进制数，计算如下，<br />\n" +
                "<img src=\"https://static.rymcu.com/article/1584363961871.png\" alt=\"进制转换公式1.png\" /><br />\n" +
                "大家可以利用这个公式计算的结果和上表进行一一对照。<br />\n" +
                "十六进制也是我们常用的进制，它的特点为逢十六进一，包括 0，1，2，3，4，5，6，7，8，9，A，B，C，D，E，F 共十六个元素。实际上十六进制是二进制的一种特殊形式，十六进制的 1 位等价于二进制的 4 位，在 C 语言编程中我们常用十六进制来表示二进制数。在实际应用中我们常常该数字之前加一个前缀来表示他的进制：“0b”表示二进制，“0x”表示十六进制。下面我们举例说明：<br />\n" +
                "0b10010010 = 0x92<br />\n" +
                "上面一个八位的二进制数转换为一个两位的十六进制数。二进制的前 4 位等于十六进制的第 1 位：<br />\n" +
                "0b1001 = 0x9<br />\n" +
                "二进制数的后 4 位等于十六进制的第 2 位：<br />\n" +
                "0b0010 = 0x2<br />\n" +
                "在计算机中，我们通常所说的二进制的 1 位也叫 1bit，8 位表示 1 个字节，也叫 1Byte。根据二进制与十六机制的关系，一个 2 位的十六进制数则可表示 1 个字节。在运用的过程中牢记 0~15 的十进制与二进制、十六进制之间的转换关系对于程序的编写有很大的好处。</p>\n" +
                "<h2>4.2 闪烁 LED 小灯</h2>\n" +
                "<p>怎么让 LED 小灯闪烁？我们最先想到的办法当然是先让 LED 小灯点亮，延时一段时间，熄灭 LED 小灯，再延时一段时间，一直循环上面的步骤就能实现 LED 小灯的闪烁了。根据第 3 章的知识我们知道点亮 LED 的语句为“led0 = 0;”,熄灭 LED 的语句为“led0 = 1;”。按照第 3 章介绍我们重新建立一个 LED 小灯闪烁的工程。程序代码设计如下：</p>\n" +
                "<pre><code class=\"language-C++\">#include&lt;reg52.h&gt; //寄存器声明头文件  \n" +
                "sbit led0 = P1^0; // 位声明，将P1.0管脚声明为led0  \n" +
                "  \n" +
                "void main()  //程序主函数入口，每个C语言程序有且只有一个  \n" +
                "{  \n" +
                " int i; //变量声明  \n" +
                " while(1) //循环  \n" +
                " {  \n" +
                " led0 = 0; //赋值管脚P1.0为低电平，点亮LED小灯  \n" +
                " for(i=0;i&lt;5000;i++);//延时一段时间  \n" +
                " led0 = 1;//熄灭LED小灯  \n" +
                " for(i=0;i&lt;5000;i++);//再延时一段时间  \n" +
                " }  \n" +
                "}  \n" +
                "</code></pre>\n" +
                "<h2>4.3\t跑马灯设计</h2>\n" +
                "<p>在我们的开发板上设计了 8 个依次排列的 LED 小灯，让小灯依次点亮和熄灭实现跑马灯的效果是我们这一节的主要内容。</p>\n" +
                "<h3>4.3.1 硬件设计</h3>\n" +
                "<p>8 个 LED 小灯的硬件电路设计原理图如下图所示：<br />\n" +
                "<img src=\"https://static.rymcu.com/article/1584364202185.png\" alt=\"图4-1-8位跑马灯设计原理图.png\" /><br />\n" +
                "如上图所示，8 个 LED 小灯 LED0-LED7 的正极和电源 VCC 之间均串联了一个 1K 的限流电阻。LED7-LED0 的负极与 74HC573 锁存器的 Q0-Q7 一一相连接。锁存器 74HC573 的功能我们这里不详细介绍，把它的 D0-D7 与 Q0-Q7 之间看作是电气上一一联通的。由图所示，锁存器的 D0-D7 和单片机的 P1.7-P1.0 是一一连接的。因此，LED 小灯 LED7-LED0 的负极与单片机的 P1.7~P1.0 管脚一一相连，在单片机程序中通过控制 P1.7-P1.0 管脚的高低电平便可控制 8 个 LED 小灯的亮灭。<br />\n" +
                "该实验要实现的功能为：首先点亮 LED0，然后延迟一段时间，熄灭 LED0，熄灭 LED0 点亮 LED1，延迟一段时间，熄灭 LED1 点亮 LED2，延迟一段时间，一直到熄灭 LED6 点亮 LED7，依照上面的步骤一直循环下去，便实现了一个简单的跑马灯的效果。</p>\n" +
                "<h3>4.3.2 软件设计</h3>\n" +
                "<p>前面我们试验中都是只对 P1.0 这个管脚进行赋值，来控制 LED 小灯 led0 的亮灭。实际在编写程序的过程中我们可以对 P1 寄存器进行直接赋值来同时控制 8 个 LED 小灯。<br />\n" +
                "<img src=\"https://static.rymcu.com/article/1584364354304.png\" alt=\"表4-2-P1寄存器对照表.png\" /><br />\n" +
                "如上表所示，P1 寄存器是一个 8 位的寄存器，最高位到最低位依次对应的 P1.7 管脚到 P1.0 管脚。点亮某个 LED 小灯的二进制，十六进制赋值如上表所示。例如 P1 = 0xFE;表示点亮 led0,P1=0x7F;则表示点亮 led7。在软件代码设计时，我们想到的第一个方法为依次点亮小灯并延时，代码如下所示。</p>\n" +
                "<pre><code class=\"language-C++\">#include&lt;reg52.h&gt; //加载头文件\n" +
                "int i;\n" +
                "\n" +
                "void main()//主函数入口\n" +
                "{\n" +
                "\tP1 = 0xFE; //点亮LED0\n" +
                "\tfor(i=0;i&lt;5000;i++);//延时一段时间\n" +
                "\tP1 = 0xFD; //点亮LED1\n" +
                "\tfor(i=0;i&lt;5000;i++);//延时一段时间\n" +
                "\tP1 = 0xFB; //点亮LED2\n" +
                "\tfor(i=0;i&lt;5000;i++);//延时一段时间\n" +
                "\tP1 = 0xF7; //点亮LED3\n" +
                "\tfor(i=0;i&lt;5000;i++);//延时一段时间\n" +
                "\tP1 = 0xEF; //点亮LED4\n" +
                "\tfor(i=0;i&lt;5000;i++);//延时一段时间\n" +
                "\tP1 = 0xDF; //点亮LED5\n" +
                "\tfor(i=0;i&lt;5000;i++);//延时一段时间\n" +
                "\tP1 = 0xBF; //点亮LED6\n" +
                "\tfor(i=0;i&lt;5000;i++);//延时一段时间\n" +
                "\tP1 = 0x7F; //点亮LED7\n" +
                "\tfor(i=0;i&lt;5000;i++);//延时一段时间\n" +
                "}  \n" +
                "</code></pre>\n" +
                "<p>我们对代码进行一下小的改进，这个方法这里称之为“左移取反”法，这个方法在很多的应用中都能用到，非常实用。代码如下图所示。</p>\n" +
                "<pre><code class=\"language-C++\">#include&lt;reg52.h&gt; //加载头文件\n" +
                "int i;\n" +
                "int flag=0;\n" +
                " \n" +
                "void main()//主函数入口\n" +
                "{ \n" +
                " P0 = 0xff;\n" +
                "\twhile(1)\n" +
                "\t{\n" +
                "\t\tP1 = ~(0x01&lt;&lt;flag);//P1的值等于1左移flag位后取反，点亮第flag位LED小灯亮\n" +
                "\t\tfor(i=0;i&lt;25000;i++);//延时一段时间\n" +
                "\n" +
                "\t\tif(flag&gt;=8)\t  //flag大于7时，置零，从零开始\n" +
                "\t\t{\n" +
                "\t\t\tflag=0;\n" +
                "\t\t}\n" +
                "\t\telse\n" +
                "\t\t{\n" +
                "\t\t\tflag++;\t //flag累加\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "</code></pre>\n" +
                "<p>我们对上面代码进行分析，flag 是一个从 0 到 7 依次循环的数，P1 等于 1 向左移 flag 位再取反。当 flag 等于 2 时，0b0000,0001 左移 2 位等于 0b0000,0100,再取反等于 0b1111,1011=0xFB,并赋值给 P1，点亮了小灯 led2。同理，当 flag 等于 6 时，0b0000,0001 左移 6 位等于 0b0100,0000,再取反等于 0b1011,1111=0xBF 并赋值给 P1，点亮了小灯 led6。flag 为其他值时，大家可以进行一一分析。</p>\n" +
                "<h3>4.3.3 下载验证</h3>\n" +
                "<p>将程序通过 STC-isp 软件下载到单片机，观察 8 个 LED 小灯效果与设想的效果是否一致？至此，本章的内容讲解完毕，内容包括进制转换的基础知识、LED 小灯闪速程序以及跑马灯的两种程序。大家在动手操作的过程中多多下载到单片机中观察现象，加深印象。</p>\n";
        System.out.println(getKeywords(title, content));
        System.out.println(getNewsSummary(title, content, 200));

    }

    public static List<TagNlpDTO> getKeywords(String title, String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        // api 限制内容不能超过 3000 字
        if (content.length() > MAX_CONTENT_LENGTH) {
            content = content.substring(0, MAX_CONTENT_LENGTH);
        }
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>(1);

        // 新闻摘要接口
        JSONObject res = client.keyword(title, Html2TextUtil.getContent(content), options);
        List<TagNlpDTO> list = JSON.parseArray(res.get("items").toString(), TagNlpDTO.class);
        return list;
    }

    public static String getTopic(String title, String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        // api 限制内容不能超过 3000 字
        if (content.length() > MAX_CONTENT_LENGTH) {
            content = content.substring(0, MAX_CONTENT_LENGTH);
        }
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>(1);

        // 新闻摘要接口
        JSONObject res = client.topic(title, Html2TextUtil.getContent(content), options);
        return res.toString(2);
    }

    public static String getNewsSummary(String title, String content, int maxSummaryLen) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        // api 限制内容不能超过 3000 字
        if (content.length() > MAX_CONTENT_LENGTH) {
            content = content.substring(0, MAX_CONTENT_LENGTH);
        }
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>(1);
        options.put("title", title);

        // 新闻摘要接口
        JSONObject res = client.newsSummary(Html2TextUtil.getContent(content), maxSummaryLen, options);
        return res.getString("summary");
    }

}
