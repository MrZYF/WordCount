package wordCount;

import java.io.BufferedReader;
import java.io.File;
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
	static int codeLine = 0;
	static int blankLine = 0;
	static int commentLine = 0;
	static boolean comment = false;
	static String[] stopWords;

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

	// 判断一行为代码行、空行还是注释行
	private static void lineType(String line) {
		if (comment) {
			// ...*/的情况
			if (line.matches("[\\s\\S]*\\*/[\\s\\S]*")) {
				comment = false;
				if (line.matches("[\\s\\S]*\\*/\\s*")) {
					commentLine++;
				} else {
					codeLine++;
				}
				return;
			}
			commentLine++;
			return;
		}
		// 空行
		if (line.matches("\\s*\\S?\\s*")) {
			blankLine++;
			return;
		}
		// //...和/*...*/的情况
		if (line.matches("\\s*\\S?\\s*//[\\s\\S]*") || line.matches("\\s*\\S?\\s*/\\*[\\s\\S]*\\*/\\s*")) {
			commentLine++;
			return;
		}
		// /*...的情况
		if (line.matches("\\s*\\S?\\s*/\\*[\\s\\S]*")) {
			commentLine++;
			comment = true;
			return;
		}
		codeLine++;
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
			lineType(line);
		}
		while ((line = reader.readLine()) != null) {
			content = content + "\n" + line;
			lineNumber++;
			lineType(line);
		}
		reader.close();
		comment = false;
		return content;
	}

	public static void output(String inputPath, PrintWriter output) throws IOException {
		File file = new File(inputPath);
		// 判断输入文件路径是否存在
		if (!file.exists()) {
			System.out.println("Input file doesn't exists!");
			return;
		}

		String content = getFileContent(file);
		if (c) {
			output.println(inputPath + ", 字符数: " + (content.length() - lineNumber + 1));
		}
		if (w) {
			String[] words = content.split(",| |\n|\t"); // 单词分隔符
			int number = 0;
			for (int i = 0; i < words.length; i++) {
				// 单词计数，排除双分隔符产生的空字符串
				if (!(words[i].equals("") || words[i] == null)) {
					number++;
					if (e) { // 停用词
						for (int j = 0; j < stopWords.length; j++) {
							if (words[i].equals(stopWords[j])) {
								number--;
							}
						}
					}
				}
			}
			output.println(inputPath + ", 单词数: " + number);
		}
		if (l) {
			output.println(inputPath + ", 行数: " + lineNumber);
		}
		if (a) {
			output.println(inputPath + ", 代码行/空行/注释行: " + codeLine + "/" + blankLine + "/" + commentLine);
		}
	}

	public static void outputAllFiles(File file, PrintWriter output) {
		File[] tempList = file.listFiles();
		String filePath, fileName;
		// 遍历文件数组
		for (int i = 0; i < tempList.length; i++) {
			// 如果文件数组中第i个元素为文件
			if (tempList[i].isFile()) {
				fileName = tempList[i].getName();
				String fileMatch;
				if (inputPath.contains("\\")) {
					String input = inputPath.substring(inputPath.lastIndexOf("\\") + 1);
					fileMatch = input;
					filePath = tempList[i].getPath();
				} else {
					fileMatch = inputPath;
					filePath = tempList[i].getPath().substring(System.getProperty("user.dir").length() + 1); // 相对路径
				}
				// 通配符转正则表达式
				fileMatch = fileMatch.replaceAll("\\.", "\\\\.");
				fileMatch = fileMatch.replaceAll("\\*", ".*");
				fileMatch = fileMatch.replaceAll("\\?", ".");
				if (fileName.matches(fileMatch)) {
					try {
						output(filePath, output);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			// 如果文件数组中第i个元素为目录
			else if (tempList[i].isDirectory()) {
				outputAllFiles(tempList[i], output);
			}
		}
	}

	public static void main(String[] args) throws IOException {
		argsProcessing(args);
		if (e) {
			File stopListFile = new File(stopListPath);
			if (!stopListFile.exists()) {
				System.out.println("StopList doesn't exists!");
				return;
			}
			BufferedReader reader = new BufferedReader(new FileReader(stopListFile));
			String stopList = reader.readLine();
			reader.close();
			stopWords = stopList.split(" ");
		}
		createNewFile(outputPath);
		PrintWriter output = null;
		output = new PrintWriter(outputPath);
		if (s) {
			File file;
			if (inputPath.contains(":\\")) { // 完整路径
				file = new File(inputPath.substring(0, inputPath.lastIndexOf("\\")));
			} else { // 相对路径，默认本文件夹
				String path = System.getProperty("user.dir"); // 当前文件夹路径
				if (inputPath.contains("\\")) {
					path = path + "\\" + inputPath.substring(0, inputPath.lastIndexOf("\\"));
				}
				file = new File(path);
			}
			outputAllFiles(file, output);
		} else {
			output(inputPath, output);
		}
		output.close();
	}

}
