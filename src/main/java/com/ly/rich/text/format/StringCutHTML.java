package com.ly.rich.text.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * @author http://hi.baidu.com/vnplalvyulin
 * 
 * @version 1.0
 *
 *          按字节长度截取字符串(支持截取带HTML代码样式的字符串)
 * @param param
 *            将要截取的字符串参数
 * @param length
 *            截取的字节长度
 * @param end
 *            字符串末尾补上的字符串
 * @return 返回截取后的字符串
 */

public class StringCutHtml {

	private static Map<String, Boolean> map = new HashMap<String, Boolean>();
	static {
		map.put("img", true);
		map.put("br", true);
		map.put("a", true);
	}

	public static void main(String[] args) {
		String cutStr = "<p><img src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\" data-ke-src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\" alt=\"国羽里约训练最后冲刺 林丹兴奋感受奥运气氛\"> </p><p>　　林丹在训练</p><p>腾讯体育里约热内卢8月9日（文/张楠） 抵达里约已经两天，中国羽毛球队开始在这里进行奥运前的最后冲刺。而林丹也将开启自己的第二次卫冕之路，谈到抵达里约两天的感受，林丹说当自己走进里约中心四号馆的时候已经开始兴奋起来。</p><p>前天下午，在圣保罗集训营训练了一周时间的中国羽毛球队终于抵达里约。当时林丹还说，没有兴奋的感觉，只是觉得有些着急，因为很多提前来到里约的队伍已经提前在比赛场训练。昨天下午，林丹也是第一次迈进球馆开始正式的训练，他说当自己拿起球包准备来赛场训练的时候，就已经开始兴奋起来。因为比其他对手少了几天的训练时间，虽然每天在场馆训练时间只有一个小时，但是林丹还是希望充分利用这些时间适应场地和风向，多一些有球训练。今天下午，球队也是在这里进行了有球训练。</p>";
		System.out.println(subStringHTML(cutStr, 100, ""));
	}

	public static String subStringHTML(String param, int length, String end) {
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++) {
			temp = param.charAt(i);
			if (temp == '<') {
				isCode = true;
			} else if (temp == '&') {
				isHTML = true;
			} else if (temp == '>' && isCode) {
				n = n - 1;
				isCode = false;
			} else if (temp == ';' && isHTML) {
				isHTML = false;
			}

			if (!isCode && !isHTML) {
				n = n + 1;
				// UNICODE码字符占两个字节
				if ((temp + "").getBytes().length > 1) {
					n = n + 1;
				}
			}

			result.append(temp);
			if (n >= length) {
				break;
			}
		}
		result.append(end);
		// 取出截取字符串中的HTML标记
		// String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)",
		// "$1$2");
		// 去掉不需要结素标记的HTML标记
		// String temp_result = result.toString().replaceAll(
		// "</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>",
		// "");
		//取两个img
		String str=result.toString();
		Pattern p1 = Pattern.compile("(<img[^<>]*>)");
		Matcher m1 = p1.matcher(str);
		StringBuilder accum = new StringBuilder();
		int count=0,endIndex=0;
		while (m1.find()) {
			if (++count == 2)
				endIndex=m1.end();
		}
		String res=endIndex==0?str:str.substring(0,endIndex);
		accum.append(res);
		
		// 去掉成对的HTML标记
		
		String temp_result=res.replaceAll("<[ ]*/*[ ]*br[ ]*/*>", "");
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
		temp_result=temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);

		List<String> endHTML = new ArrayList<String>();
		while (m.find()) {
			if (null == map.get(m.group(1).toString()))
				endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--) {
			accum.append("</");
			accum.append(endHTML.get(i));
			accum.append(">");
		}

		return accum.toString();
	}

}
