package wordCount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
	// 设置参数和文件
	static boolean c = false; // 返回文件的字符数
	static boolean w = false; // 返回文件的单词总数
	static boolean l = false; // 返回文件的总行数
	static boolean o = false; // 将结果输出到指定文件
	static boolean s = false; // 递归处理目录下符合条件的文件
	static boolean a = false; // 返回更复杂的数据（代码行 / 空行 / 注释行）
	static boolean e = false; // 停用词表，统计文件单词总数时，不统计该表中的单词
	static String inputPath = null;
	static String outputPath = "result.txt";
	static String stopListPath = null;
	static int lineNumber = 0;

	// 参数处理
	private static void argsProcessing(String[] args) {
		if (args.length == 0) {
			System.out.println("Please input parameters!");
			return;
		}
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-c":
				c = true;
				break;
			case "-w":
				w = true;
				break;
			case "-l":
				l = true;
				break;
			case "-o":
				o = true;
				i++;
				outputPath = args[i];
				break;
			case "-s":
				s = true;
				break;
			case "-a":
				a = true;
				break;
			case "-e":
				e = true;
				i++;
				stopListPath = args[i];
				break;
			default:
				inputPath = args[i];
				break;
			}
		}
		if (inputPath == null) {
			System.out.println("Please input the input_file_name");
			return;
		}
	}

	private static void createNewFile(String path) {
		File file = new File(path);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	// 读文件内容
	private static String getFileContent(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		String content = "";
		// 一次读入一行，直到读入null为文件结束
		if ((line = reader.readLine()) != null) {
			content = line;
			lineNumber++;
		}
		while ((line = reader.readLine()) != null) {
			content = content + "\n" + line;
			lineNumber++;
		}
		reader.close();
		return content;
	}

	public static void main(String[] args) throws IOException {
		argsProcessing(args);
		// 判断输入文件路径是否存在
		File file = new File(inputPath);
		if (!file.exists()) {
			System.out.println("Input file doesn't exists!");
			return;
		}

		String content = getFileContent(file);
		createNewFile(outputPath);
		PrintWriter output = null;
		try {
			output = new PrintWriter(outputPath);
			if (c) {
				output.println(inputPath + ", 字符数: " + content.length());
			}
			if (w) {
				String[] words = content.split(",| |\n|\t"); // 单词分隔符
				int number = 0;
				for (int i = 0; i < words.length; i++) {
					// 单词计数，排除双分隔符产生的空字符串
					if (!(words[i].equals("") || words[i] == null)) {
						number++;
					}
				}
				output.println(inputPath + ", 单词数: " + number);
			}
			if (l) {
				output.println(inputPath + ", 行数: " + lineNumber);
			}
			
			output.close();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
	}

}
