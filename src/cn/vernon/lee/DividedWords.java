package cn.vernon.lee;

import java.text.BreakIterator;

/**
 * 使用BreakIterator分割文字或句子
 * @since 2014-11-24
 * @author huailiang
 */
public class DividedWords {
	public static void main(String[] args) {
		String stringToExamine = "ก้ำเปลี่ยนภาษาเมนูใน某些类型.BreakIterator的创, your computer must be set up "
				+ "for Thai言語のテスト언어테스트 الذهب  جميلة جدทองคำสวยหมายเลข 10 ของอัสซาดเป็นผู้วิเศษ ASF";
		// print each word in order
		BreakIterator boundary = BreakIterator.getSentenceInstance();
		boundary.setText(stringToExamine);
		printEachForward(boundary, stringToExamine);

		BreakIterator boundary2 = BreakIterator.getWordInstance();
		boundary2.setText(stringToExamine);
		printEachForward(boundary2, stringToExamine);

		// 比较符合要求，部分类文字如中日韩文字OK 泰语不行
		BreakIterator boundary3 = BreakIterator.getLineInstance();
		boundary3.setText(stringToExamine);
		printEachForward(boundary3, stringToExamine);

		BreakIterator boundary4 = BreakIterator.getCharacterInstance();
		boundary4.setText(stringToExamine);
		printEachForward(boundary4, stringToExamine);

	}

	public static void printEachForward(BreakIterator boundary, String source) {
		int start = boundary.first();
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
				.next()) {
			System.out.println(source.substring(start, end));
		}
	}

	public static void printEachBackward(BreakIterator boundary, String source) {
		int end = boundary.last();
		for (int start = boundary.previous(); start != BreakIterator.DONE; end = start, start = boundary
				.previous()) {
			System.out.println(source.substring(start, end));
		}
	}

	public static void printFirst(BreakIterator boundary, String source) {
		int start = boundary.first();
		int end = boundary.next();
		System.out.println(source.substring(start, end));
	}

	public static void printLast(BreakIterator boundary, String source) {
		int end = boundary.last();
		int start = boundary.previous();
		System.out.println(source.substring(start, end));
	}

	public static void printAt(BreakIterator boundary, int pos, String source) {
		int end = boundary.following(pos);
		int start = boundary.previous();
		System.out.println(source.substring(start, end));
	}

	public static int nextWordStartAfter(int pos, String text) {
		BreakIterator wb = BreakIterator.getWordInstance();
		wb.setText(text);
		int last = wb.following(pos);
		int current = wb.next();
		while (current != BreakIterator.DONE) {
			for (int p = last; p < current; ++p) {
				if (Character.isLetter(text.charAt(p))) {
					return last;
				}
			}
			last = current;
			current = wb.next();
		}
		return BreakIterator.DONE;
	}
}
