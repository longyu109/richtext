package com.ly.rich.text.format;

import java.io.File;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class RichTextFormatter {

	// 制表符，换行
	public final static String CTRL_STRING = "[\\t\\n\\f\\r]";

	public final static String CTRL_STRING_BR = "[\\t\\f\\r]";

	// 生僻字
	public final static String NON_BMP = "[^\\u0000-\\uFFFF]";

	public final static String UNSUPPORT_CHAR_SURROGATE = "\uFFFD";

	public final static String FINAL_IDENTIFIER_CUT = "<span style=\"display:none\" data-preview=\"true\"></span>";

	private final static OutputSettings OUTPUTSETTINGS = new Document.OutputSettings().prettyPrint(false);

	public final static String demo1 = "<img src=\"1.jpg\" data-large=\"123\"/><p style=\"color:red;width:100;height:200\">元素的内容会显示在浏览器中。</p><img src=\"2.jpg\" data-large=\"123\"/> <p>元素的内容会显示在浏览器的标题栏中。</p> <p>123<br/>234<br/>567<br/></p>";

	public static Whitelist xmRichIntro() {
		return new Whitelist()
				.addTags("font", "span", "div", "p", "ol", "ul", "li", "blockquote", "sub", "sup", "i", "u", "strike",
						"s", "del", "img", "br", "b", "strong", "em", "a")
				.addAttributes("img", "src", "alt", "data-large", "data-large-width", "data-large-height",
						"data-preview", "data-preview-width", "data-preview-height")
				.addAttributes("a", "href", "target", "rel", "card").addAttributes("p", "style");
	}

	public static Whitelist xmRichIntroCut() {
		return new Whitelist().addTags("img", "br", "p").addAttributes("img", "src", "alt", "title", "data-large",
				"data-large-width", "data-large-height", "data-preview", "data-preview-width", "data-preview-height");
	}

	public static Whitelist xmRichIntroCut4Br() {
		return new Whitelist().addTags("br");
	}

	/**
	 * 过滤特殊字符，只留纯文本
	 * 
	 */

	public static String cleanTextAttribute(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		String newText = text.replaceAll(CTRL_STRING, "");
		newText = newText.replaceAll(NON_BMP, UNSUPPORT_CHAR_SURROGATE);
		newText = StringEscapeUtils.unescapeHtml(newText);

		newText = StringEscapeUtils.unescapeHtml(Jsoup.clean(newText, Whitelist.none()));
		return newText;
	}

	/**
	 * 过滤特殊字符，只留<br>
	 * 
	 */

	public static String cleanAndRemainBr(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		String newText = text.replaceAll(CTRL_STRING, "");
		newText = newText.replaceAll(NON_BMP, UNSUPPORT_CHAR_SURROGATE);
		newText = StringEscapeUtils.unescapeHtml(newText);
		newText = StringEscapeUtils.unescapeHtml(Jsoup.clean(newText, xmRichIntroCut4Br()));
		return newText;
	}

	/**
	 * 过滤特殊字符，将/n换成<br>
	 * ,只留<br>
	 * 
	 */

	public static String cleanAndRemainBr2(String text) {
		if (StringUtils.isEmpty(text)) {
			return text;
		}
		String newText = text.replaceAll(CTRL_STRING_BR, "");
		newText = text.replaceAll("/n", "<br/>");
		newText = newText.replaceAll(NON_BMP, UNSUPPORT_CHAR_SURROGATE);
		newText = StringEscapeUtils.unescapeHtml(newText);
		newText = StringEscapeUtils.unescapeHtml(Jsoup.clean(newText, xmRichIntroCut4Br()));
		return newText;
	}

	/**
	 * 过滤富文本，去掉特殊字符
	 * 
	 */

	public static String cleanRichIntro(String intro) {
		if (StringUtils.isEmpty(intro)) {
			return intro;
		}
		String newText = intro.replaceAll(CTRL_STRING_BR, "");
		String newIntro = newText.replaceAll(NON_BMP, UNSUPPORT_CHAR_SURROGATE);
		newIntro = StringEscapeUtils.unescapeHtml(newIntro);
		return StringEscapeUtils.unescapeHtml(Jsoup.clean(newIntro, "", xmRichIntro(), OUTPUTSETTINGS));
	}

	/**
	 * 过滤富文本，按字数过滤
	 * 
	 */

	public static String richTextCut4FixedWords(String html, int words) {
		if (!StringUtils.isNotEmpty(html)) {
			return html;
		}
		if (words < 0) {
			throw new IllegalArgumentException("the length of cut words is non-negative");
		}
		String startStr = StringEscapeUtils.unescapeHtml(html);
		String secondStr = startStr.replaceAll(CTRL_STRING_BR, "");
		String thirdStr = secondStr.replaceAll(NON_BMP, UNSUPPORT_CHAR_SURROGATE);
		String fourthStr = Jsoup.clean(thirdStr, "", xmRichIntro(), OUTPUTSETTINGS);
		String fifStr = parseHtml4OnlyRemainPartStyle(fourthStr);

		StringBuilder accum = new StringBuilder();
		boolean isCut = false;

		String res = StringCutHTML.subStringHTML(fifStr, words, "");
		accum.append(res);
		if (fifStr.length() > accum.length()) {
			// 0代表裁剪过
			isCut = true;
			accum.append("...");
		} else {
			isCut = false;
		}
		if (isCut) {
			accum.append(FINAL_IDENTIFIER_CUT);
		}
		return accum.toString();
	}

	public static String cleanIntro(String intro) {
		if (StringUtils.isEmpty(intro)) {
			return intro;
		}
		String newIntro = intro.replaceAll(NON_BMP, UNSUPPORT_CHAR_SURROGATE);
		return StringEscapeUtils.unescapeHtml(Jsoup.clean(newIntro, Whitelist.none()));
	}

	private static String parseHtml4OnlyRemainPartStyle(String text) {
		// Document doc = Jsoup.connect("http://www.baidu.com/").get();
		// Document doc = Jsoup.parse(text, "", Parser.xmlParser());
		Document doc = Jsoup.parse(text);
		doc.outputSettings().prettyPrint(false);
		Elements hrefs = doc.select("p[style]");
		for (Element element : hrefs) {
			String style = element.attr("style");
			if (null != style) {
				String[] styles = style.split(";");
				if (null != styles && styles.length != 0) {
					for (String ss : styles) {
						if (ss.trim().startsWith("color")) {
							element.attr("style", ss);
							break;
						}
					}
				}
			}
		}
		String res = doc.body().html();
		return res;
	}

	public static void main(String[] args) throws Exception {

		// String str=cleanRichIntro(demo1);
		// System.out.println(str);

		// String src = Jsoup.parse(new File("/Users/nali/Desktop/test2.html"),

		String testSrc = "<p><img src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\" data-ke-src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\" alt=\"国羽里约训练最后冲刺 林丹兴奋感受奥运气氛\"> </p><p>　　林丹在训练</p><p>腾讯体育里约热内卢8月9日（文/张楠） 抵达里约已经两天，中国羽毛球队开始在这里进行奥运前的最后冲刺。而林丹也将开启自己的第二次卫冕之路，谈到抵达里约两天的感受，林丹说当自己走进里约中心四号馆的时候已经开始兴奋起来。</p><p>前天下午，在圣保罗集训营训练了一周时间的中国羽毛球队终于抵达里约。当时林丹还说，没有兴奋的感觉，只是觉得有些着急，因为很多提前来到里约的队伍已经提前在比赛场训练。昨天下午，林丹也是第一次迈进球馆开始正式的训练，他说当自己拿起球包准备来赛场训练的时候，就已经开始兴奋起来。因为比其他对手少了几天的训练时间，虽然每天在场馆训练时间只有一个小时，但是林丹还是希望充分利用这些时间适应场地和风向，多一些有球训练。今天下午，球队也是在这里进行了有球训练。</p><p>123<br/>123<br/></p>";
		String res = richTextCut4FixedWords(testSrc, 100);
		String res1 = richTextCut4FixedWords(testSrc, 200);
		String res2 = richTextCut4FixedWords(testSrc, 400);
		String res3 = richTextCut4FixedWords(testSrc, 600);
		String res4 = richTextCut4FixedWords(testSrc, 800);

		System.out.println(res);
		System.out.println(res1);
		System.out.println(res2);
		System.out.println(res3);
		System.out.println(res4);
	}

}
