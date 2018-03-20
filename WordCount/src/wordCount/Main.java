package wordCount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
	// ���ò������ļ�
	static boolean c = false; // �����ļ����ַ���
	static boolean w = false; // �����ļ��ĵ�������
	static boolean l = false; // �����ļ���������
	static boolean o = false; // ����������ָ���ļ�
	static boolean s = false; // �ݹ鴦��Ŀ¼�·����������ļ�
	static boolean a = false; // ���ظ����ӵ����ݣ������� / ���� / ע���У�
	static boolean e = false; // ͣ�ôʱ�ͳ���ļ���������ʱ����ͳ�Ƹñ��еĵ���
	static String inputPath = null;
	static String outputPath = "result.txt";
	static String stopListPath = null;
	static int lineNumber = 0;
	static int codeLine = 0;
	static int blankLine = 0;
	static int commentLine = 0;
	static boolean comment = false;
	static String[] stopWords;

	// ��������
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

	// �ж�һ��Ϊ�����С����л���ע����
	private static void lineType(String line) {
		if (comment) {
			// ...*/�����
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
		// ����
		if (line.matches("\\s*\\S?\\s*")) {
			blankLine++;
			return;
		}
		// //...��/*...*/�����
		if (line.matches("\\s*\\S?\\s*//[\\s\\S]*") || line.matches("\\s*\\S?\\s*/\\*[\\s\\S]*\\*/\\s*")) {
			commentLine++;
			return;
		}
		// /*...�����
		if (line.matches("\\s*\\S?\\s*/\\*[\\s\\S]*")) {
			commentLine++;
			comment = true;
			return;
		}
		codeLine++;
	}

	// ���ļ�����
	private static String getFileContent(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		String content = "";
		// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
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
		// �ж������ļ�·���Ƿ����
		if (!file.exists()) {
			System.out.println("Input file doesn't exists!");
			return;
		}

		String content = getFileContent(file);
		if (c) {
			output.println(inputPath + ", �ַ���: " + (content.length() - lineNumber + 1));
		}
		if (w) {
			String[] words = content.split(",| |\n|\t"); // ���ʷָ���
			int number = 0;
			for (int i = 0; i < words.length; i++) {
				// ���ʼ������ų�˫�ָ��������Ŀ��ַ���
				if (!(words[i].equals("") || words[i] == null)) {
					number++;
					if (e) { // ͣ�ô�
						for (int j = 0; j < stopWords.length; j++) {
							if (words[i].equals(stopWords[j])) {
								number--;
							}
						}
					}
				}
			}
			output.println(inputPath + ", ������: " + number);
		}
		if (l) {
			output.println(inputPath + ", ����: " + lineNumber);
		}
		if (a) {
			output.println(inputPath + ", ������/����/ע����: " + codeLine + "/" + blankLine + "/" + commentLine);
		}
	}

	public static void outputAllFiles(File file, PrintWriter output) {
		File[] tempList = file.listFiles();
		String filePath, fileName;
		// �����ļ�����
		for (int i = 0; i < tempList.length; i++) {
			// ����ļ������е�i��Ԫ��Ϊ�ļ�
			if (tempList[i].isFile()) {
				fileName = tempList[i].getName();
				String fileMatch;
				if (inputPath.contains("\\")) {
					String input = inputPath.substring(inputPath.lastIndexOf("\\") + 1);
					fileMatch = input;
					filePath = tempList[i].getPath();
				} else {
					fileMatch = inputPath;
					filePath = tempList[i].getPath().substring(System.getProperty("user.dir").length() + 1); // ���·��
				}
				// ͨ���ת������ʽ
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
			// ����ļ������е�i��Ԫ��ΪĿ¼
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
			if (inputPath.contains(":\\")) { // ����·��
				file = new File(inputPath.substring(0, inputPath.lastIndexOf("\\")));
			} else { // ���·����Ĭ�ϱ��ļ���
				String path = System.getProperty("user.dir"); // ��ǰ�ļ���·��
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
