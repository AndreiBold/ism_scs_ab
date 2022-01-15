package eu.ase.ism.scs.obf;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Obfuscator {

    static final String[] symbols = {
            "int", "main", "char", "printf", "scanf", "_getch", "return", "0",
            "for", "sizeof", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "void", "while", "unsigned", "long", "short", "double", "float",
            "do", "true", "false", "if", "else", "goto", "break", "case",
            "switch", "const", "continue", "enum", "struct", "typedef", "fopen",
            "fclose", "ftell", "fseek", "fread", "fwrite", "feof", "strstr",
            "strcpy", "strlen", "strcat", "strchr", "memcmp", "memcpy", "malloc",
            "free", "memset", "20", "10", "324", "45", "90", "9808", "50", "100",
            "200", "2000", "93"
    };

    static final String[] obfuscations = {
            "___", "_____","__", "_p_", "_s_", "_glich_", "_r_t_", "Q", "_b_ey",
            "_wei_ght", "I", "Z", "E", "A", "S", "G", "T", "B", "P", "_he_lo",
            "_v_", "____", "______", "_h_", "_u_", "_l_", "_ru_n", "_x_o_r", "n_ot",
            "o_k", "do_wn", "r_un", "b_tc_", "_de_t", "wa_tch", "ti_me_", "pro_cess",
            "_li_st", "o_bj", "t_d_", "_ma_sk", "unm_ask", "s_ay", "_wa_nt", "sm_art",
            "du_mb", "_ye_p", "_st_rr", "_to_dp", "_cm_", "_cat_", "_m_", "re_al_oc",
            "f_ly", "br_ai_n", "_n_", "F", "V", "K", "C", "N", "Y", "R", "W", "D", "H",
            "M"
    };

    public static List<String> computeMacrosList() {
        List<String> macrosList = new ArrayList<>();

        for(int i = 0; i < symbols.length; i++) {
            for(int j = 0; j < obfuscations.length; j++) {
                if(i == j) {
                    String macro = "#define " + obfuscations[j] + " " + symbols[i];
                    macrosList.add(macro);
                }
            }
        }
        return macrosList;
    }

    public static class IncorrectFileExtensionException extends  Exception {
        public IncorrectFileExtensionException() {
            super("File extension not allowed! Please provide a .c file!");
        }
    }

    public static void fileObfuscator(File inputFile) throws IncorrectFileExtensionException {
        String fileExtension = inputFile.getName().split("\\.")[1];
        String fileName = inputFile.getName().split("\\.")[0];

        if(!fileExtension.equals("c")) {
            throw new IncorrectFileExtensionException();
        }

        List<String> contentList = new ArrayList<>();
        List<String> macrosList = computeMacrosList();
        List<String> forObfuscationList = new ArrayList<>();
        List<String> obfuscatedList = new ArrayList<>();
        List<String> finalContentList = new ArrayList<>();

        try(BufferedReader bufferedReader =  new BufferedReader(new FileReader(inputFile))) {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                contentList.add(line);
            }

            File photo = new File("astronaut.jpg");
            byte[] photoContent = Files.readAllBytes(photo.toPath());

            Random r1 = new Random(photo.length());
            Random r2 = new Random(photo.length());
            Random r3 = new Random(photo.length());
            Random r4 = new Random((int) (new Date().getTime()/1000));

            int b1 = photoContent[r1.nextInt(r4.nextInt(1200))];
            int b2 = photoContent[r2.nextInt(r4.nextInt(15000))];
            int b3 = photoContent[r3.nextInt(r4.nextInt(30))];

            int randomNumber = ((b1 >> b2 << b3) | (b2 ^ b1)) + r4.nextInt(2000);
            Random r = new Random(randomNumber);

            String outputFileName = fileName + r.nextInt(randomNumber) + ".c";

            BufferedWriter obfuscatedFile = new BufferedWriter(new FileWriter(outputFileName));
            for(int i=0; i< contentList.size(); i++) {
                if(contentList.get(i).contains("#include") || contentList.get(i).contains("#define")) {
                    finalContentList.add(contentList.get(i));
                    finalContentList.add("\n");
                }
                else {
                    if(!contentList.get(i).contains("//")) {
                        forObfuscationList.add(contentList.get(i));
                    }
                }
            }

            finalContentList.add("\n");

            for(int j = 0; j < forObfuscationList.size(); j++) {
                String newLine = "";
                String tempLine = forObfuscationList.get(j);

                for(int k = 0; k < macrosList.size(); k++) {
                    if(forObfuscationList.get(j).contains(macrosList.get(k).split("\\ ")[2])) {
                        tempLine = tempLine.replaceAll("\\b" + macrosList.get(k).split("\\ ")[2] + "\\b",
                                macrosList.get(k).split("\\ ")[1]);

                        if(!finalContentList.contains(macrosList.get(k))) {
                            finalContentList.add(macrosList.get(k));
                            finalContentList.add("\n");
                        }
                    }
                }
                newLine = tempLine;

                obfuscatedList.add(newLine);
                obfuscatedList.add("\n");
            }

            finalContentList.addAll(obfuscatedList);

            for(int p = 0; p < finalContentList.size(); p++) {
                    obfuscatedFile.write(finalContentList.get(p));
            }
            obfuscatedFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Successfully obfuscated");
    }

    public static void main(String[] args) throws IncorrectFileExtensionException {
        fileObfuscator(new File("todo.txt"));
    }
}


