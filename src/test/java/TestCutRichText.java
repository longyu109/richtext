
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.ly.rich.text.format.RichTextFormatter;
import com.ly.rich.text.format.StringCutHtml;

public class TestCutRichText {
	
	/**
	 * 一个标签嵌套多个标签
	 * 
	 * */
	
	@Test
	public void test1(){
		String cutStr = "<p><img src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\" data-ke-src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\" alt=\"国羽里约训练最后冲刺 林丹兴奋感受奥运气氛\"></img><span>123国羽里约训练最后冲刺 林丹兴奋感受奥运气氛</span> </p><p>　　林丹在训练</p><p>腾讯体育里约热内卢8月9日（文/张楠） 抵达里约已经两天，中国羽毛球队开始在这里进行奥运前的最后冲刺。而林丹也将开启自己的第二次卫冕之路，谈到抵达里约两天的感受，林丹说当自己走进里约中心四号馆的时候已经开始兴奋起来。</p><p>前天下午，在圣保罗集训营训练了一周时间的中国羽毛球队终于抵达里约。当时林丹还说，没有兴奋的感觉，只是觉得有些着急，因为很多提前来到里约的队伍已经提前在比赛场训练。昨天下午，林丹也是第一次迈进球馆开始正式的训练，他说当自己拿起球包准备来赛场训练的时候，就已经开始兴奋起来。因为比其他对手少了几天的训练时间，虽然每天在场馆训练时间只有一个小时，但是林丹还是希望充分利用这些时间适应场地和风向，多一些有球训练。今天下午，球队也是在这里进行了有球训练。</p><p><span>123国羽里约训练最后冲刺 林丹兴奋感受奥运气氛</span><span>123国羽里约训练最后冲刺 林丹兴奋感受奥运气氛</span> </p>";
		System.out.println(RichTextFormatter.richTextCut4FixedWords(cutStr, 100));
		System.out.println(RichTextFormatter.richTextCut4FixedWords(cutStr, 200));
		System.out.println(RichTextFormatter.richTextCut4FixedWords(cutStr, 300));
		System.out.println(RichTextFormatter.richTextCut4FixedWords(cutStr, 400));
		System.out.println(RichTextFormatter.richTextCut4FixedWords(cutStr, 500));
		System.out.println(RichTextFormatter.richTextCut4FixedWords(cutStr, 600));
		System.out.println(RichTextFormatter.richTextCut4FixedWords(cutStr, 700));
		System.out.println(RichTextFormatter.richTextCut4FixedWords(cutStr, 800));
	}
	
	
	//
	@Test
	public void replaceBr(){
		String str="123<br>123<br /></br>";
		String res=str.replaceAll("<[ ]*/*[ ]*br[ ]*/*>", "");
//		System.out.println(str);
//		System.out.println(res);
	}
	
	@Test
	public void testImg() {
		String str = "<p><img src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\" data-ke-src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\" alt=\"国羽里约训练最后冲刺 林丹兴奋感受奥运气氛\"> </p><p>　　林丹在训练</p><p>腾讯体育里约热内卢8月9日（文/张楠） 抵达里约已经两天，中国羽毛球队开始在这里进行奥运前的最后冲刺。而林丹也将开启自己的第二次卫冕之路，谈到抵达里约两天的感受，林丹说当自己走进里约中心四号馆的时候已经开始兴奋起来。</p><p><img src=\"http://news.youth.cn/zc/201608/W020160810334953417789.jpg\"/></p><p>前天下午，在圣保罗集训营训练了一周时间的中国羽毛球队终于抵达里约。当时林丹还说，没有兴奋的感觉，只是觉得有些着急，因为很多提前来到里约的队伍已经提前在比赛场训练。昨天下午，林丹也是第一次迈进球馆开始正式的训练，他说当自己拿起球包准备来赛场训练的时候，就已经开始兴奋起来。因为比其他对手少了几天的训练时间，虽然每天在场馆训练时间只有一个小时，但是林丹还是希望充分利用这些时间适应场地和风向，多一些有球训练。今天下午，球队也是在这里进行了有球训练。</p>";
		Pattern p = Pattern.compile("(<img[^<>]*>)");
		Matcher m = p.matcher(str);
		StringBuilder accum = new StringBuilder();
		int count=0,end=0;
		while (m.find()) {
			String group = m.group(0);
			System.out.println(group);
			System.out.println(m.end());
			if (++count == 2)
				end=m.end();
		}
		String res=str.substring(0, end);
		String last=StringCutHtml.subStringHTML(res, res.length(), "");
		System.out.println(last);
		
		
	}
}
