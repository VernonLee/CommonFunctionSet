package cn.vernon.lee;

/**
 * 无限循环
 * 
 * @since 2015-1-7
 * @author huailiang
 */
public class Loop {
	public static void main(String args[]) {

		/**
		 * 方式一
		 */
		int i = 0;
		for (;;) {
			if (i == 100) {
				System.out.println(i + "：haha");
				break;
			}
			i++;
		}

		/**
		 * 方式二
		 */
		while (true) {
			if (i == 100) {
				System.out.println(i + "：haha");
				break;
			}
			i++;
		}

		/**
		 * 上面的效果类似于 switch case
		 */
		switch (100) {
		default:
		case 100:
			System.out.println(i + "：haha");
			break;
		}
	}
}
