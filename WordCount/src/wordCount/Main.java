package wordCount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

	// ���ļ�����
	private static String getFileContent(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
		String content = "";
		// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
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
		// �ж������ļ�·���Ƿ����
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
				output.println(inputPath + ", �ַ���: " + content.length());
			}
			if (w) {
				String[] words = content.split(",| |\n|\t"); // ���ʷָ���
				int number = 0;
				for (int i = 0; i < words.length; i++) {
					// ���ʼ������ų�˫�ָ��������Ŀ��ַ���
					if (!(words[i].equals("") || words[i] == null)) {
						number++;
					}
				}
				output.println(inputPath + ", ������: " + number);
			}
			if (l) {
				output.println(inputPath + ", ����: " + lineNumber);
			}
			
			output.close();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
	}

}
