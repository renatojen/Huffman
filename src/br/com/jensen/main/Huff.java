/*
 * 
 * Main.java
 * Author:
 * Renato Jensen Filho
 * 
 */

package br.com.jensen.main;

import br.com.jensen.beans.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Huff {

    public static void main(String[] args) {

    String str_entrada = "", auxiliar = "", str_saida = "";
    long entry = 0, exit = 0;
    int operacao = 0; // 1 = Compressao | 2 = Descompressao

    Huffman huff = new Huffman();

    //Definicao do tipo de operacao pelos argumentos
    if (args.length > 0){
        if (args[0].equals("-c")){
            operacao = 1; //compressao
        } else if (args[0].equals("-x")){
            operacao = 2; //descompressao
        } else {
            System.out.println("Argumentos em formato incorreto! Utilize -c/-x arqentrada -o arqsaida");
        }

    File entrada = new File(args[1]);    
    entry = entrada.length();    

    try{    // is representa um fluxo de entrada a partir de um arquivo
            InputStream is = new FileInputStream(entrada);
            //InputStreamReader e uma classe para converter os bytes em char
            InputStreamReader isr = new InputStreamReader(is, "ISO-8859-1");
            //BufferedReader e uma classe para armazenar os chars em memoria
            BufferedReader br = new BufferedReader(isr);            
            //            
            while ((auxiliar = br.readLine()) != null){
                str_entrada = str_entrada + auxiliar + '\n';
            }
            //remove o ultimo '\n' da string
            str_entrada = str_entrada.substring(0, str_entrada.length()-1);
            br.close();            

        }catch(Exception x){
            System.out.println(x.getMessage());
            operacao = 0;
        }
    }

    if (operacao == 1){
        str_saida = huff.compacta(str_entrada);        
    } else if (operacao == 2){
        str_saida = huff.descompacta(str_entrada);        
    }    
    
    if (operacao != 0){
        File saida = new File(args[3]);
        if (saida.exists()){
            saida.delete();
        }
        try {

            Writer filew = new BufferedWriter(new OutputStreamWriter(  new FileOutputStream(saida,true),"ISO-8859-1")   );
            filew.write(str_saida);
            filew.close();

        } catch (IOException ex) {
            Logger.getLogger(Huff.class.getName()).log(Level.SEVERE, null, ex);
        }
        exit = saida.length();
    }
    
    if(operacao==1){
        System.out.println('\n' + "Arquivo de entrada: " + entry + " bytes");
        System.out.println("Arquivo de saida: " + exit + " bytes");
        System.out.println("Taxa de compressao: " + (1 - ((double)exit/(double)entry)));
    }

}
}
