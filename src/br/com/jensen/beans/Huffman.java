/*
 * 
 * Huffman.java
 * Author:
 * Renato Jensen Filho
 * 
 */

package br.com.jensen.beans;

import java.util.*;

public class Huffman {    

    public int[] vetor_freq;
    public char[] vetor_char;
    public int num_elem;
    public int soma_freq;
    public Vector<No> vetorn = new Vector<No>();
    public Vector<No> chars = new Vector<No>();

    //seleciona o no com a menor frequencia no vetor de nos
    private No minimo(Vector<No> vetorn) {
        for (int i = vetorn.size(); i >= 1; i--) {
            for (int j=1;j<i;j++) {
                if (vetorn.get(j-1).getFreq() > vetorn.get(j).getFreq()) {
                    No aux = vetorn.get(j);
                    vetorn.set(j, vetorn.get(j - 1));
                    vetorn.set(j-1, aux);
                }
            }
        }
        return vetorn.get(0);
    }   
    //monta a arvore de Huffman para o vetor de nos
    public void montaArvore(){
        int a = vetorn.size();
        for (int i = 0; i < a - 1; i++) {
            No z = new No();
            int fx = 0, fy = 0;
            No x = minimo(vetorn);
            z.setEsquerda( x );
            fx = x.getFreq();
            vetorn.remove( x );

            No y = minimo(vetorn);
            z.setDireita( y );
            fy = y.getFreq();
            vetorn.remove( y );

            z.setFreq(fx + fy);
            vetorn.add( z );
        }
    }

    //Funcao que efetua a compactacao da string que representa o conteudo do
    //arquivo de entrada. Retorna a string codificada.
    public String compacta(String dados){
        System.out.println("Texto a ser compactado: ");
        System.out.println(dados);
        System.out.println('\n' + "Este processo pode levar alguns minutos...");
        soma_freq = 0;
        HashMap<Character, Integer> hm = new HashMap<Character, Integer>();
        int i = 0;
        for(i = 0; i<dados.length(); i++){
            char c = dados.charAt(i);
            if(hm.get(c)!= null){
                hm.put(c, (Integer)(hm.get(c))+1);
            }else{
                hm.put(c, 1);
            }
        }

        num_elem = hm.keySet().size();
        for (i=0; i<num_elem;i++){
            No n = new No(Integer.parseInt(hm.values().iterator().next().toString()),hm.keySet().iterator().next().toString().charAt(0) );
            soma_freq = soma_freq + Integer.parseInt(hm.values().iterator().next().toString());
            vetorn.add(n);
            chars.add(n);            
            hm.values().remove(hm.values().iterator().next());
        }

        montaArvore();
        No.geraCodificacao(vetorn.get(0));
        String saida_codificada = codificaString(dados);
        int num_zeros=0;
        if (saida_codificada.length() % 8 != 0){
            int aux = 8 - (saida_codificada.length() % 8);
            for (i=0; i < aux; i++){
                saida_codificada = "0" + saida_codificada;
                num_zeros++;
            }
        }
        System.out.println('\n' + "Codificacao binaria do texto: ");
        System.out.println(saida_codificada);
        System.out.println('\n' + "Aguarde um momento...");
        int dec = 0;
        char caux;
        String str_aux = "";
        String saida_final = "";
        for(i=0; i < saida_codificada.length()/8; i++){
            str_aux = saida_codificada.substring(i*8,(i*8)+8);
            dec = Integer.parseInt(str_aux,2); //o 2 eh pra converter com base binaria
            //System.out.println("decimal: " + dec);
            caux = (char) dec;
            saida_final = saida_final + caux;
            if (dec == 13){
               saida_final = saida_final + "<13>"; 
            }
        }
        
        System.out.println('\n' + "Texto original codificado: ");
        System.out.println(saida_final);
        //passa os codewords pra uma string, utilizando ":" como separador e
        //":x:" demarcando o fim dos codewords, onde x=num_zeros
        String codigo = "";
        for (i=0; i< chars.size(); i++){
            codigo = codigo + chars.get(i).getSimb() + chars.get(i).getCod() + ":";
        }
        codigo = codigo + num_zeros + ":";
        //inclui os codewords no inicio na string que sera salva no arquivo de saida
        saida_final = codigo + saida_final;
        System.out.println('\n' + "Codificacao Concluida!");
        return saida_final;
    }

    //Converte a string do arquivo de entrada para uma string de codewords
    private String codificaString (String entrada){
        int i =0;
        String saida = "";
        for (i=0; i< entrada.length(); i++){
            saida = saida + converteChar(entrada.charAt(i));
        }
        return saida;
    }

    //Converte o caractere para o seu codeword previamente definido
    private String converteChar(char a){
        int i=0;
        String code = "";
        for (i=0; i<chars.size(); i++){
            if (chars.get(i).getSimb() == a){
                code = chars.get(i).getCod();
            }
        }
        return code;

    }  

    //Funcao que efetua a descompactacao da string que representa o conteudo do
    //arquivo de entrada. Retorna a string decodificada.
    public String descompacta(String entrada){
        System.out.println("Texto a ser descompactado: ");
        System.out.println(entrada.substring((achaPos(entrada)+1),entrada.length()));
        System.out.println('\n' + "Este processo pode levar alguns minutos...");
        int i=0;
        String codificacao = "";
        char c = 0;
        for (i=0; i < achaPos(entrada)-2;i++){
            No n = new No();
            codificacao = "";
            c = entrada.charAt(i);            
            n.setSimb(c);
            //System.out.print("Caracter: " + c + '\n');
            i++;
            c = entrada.charAt(i);
            while (c != ':'){
                codificacao = codificacao + c;
                //System.out.print("Bit adicionado: " + c + '\n');
                //System.out.print("String da Codeword: " + codificacao + '\n');
                i++;
                c = entrada.charAt(i);
            }
            n.setCod(codificacao);
            n.setFreq(0);
            chars.add(n);            
        }

        String binario = "";
        // monta a string com a codificacao binaria dos caracteres
        for(i= achaPos(entrada)+1; i< entrada.length(); i++){
            //esta verificacao e necessaria pois quando um dos caracteres
            //codificados e o decimal 13 da tabela ASCII (que representa a
            //quebra de linha do enter), ele e convertido pro caractere
            //referente ao decimal 10 da tabela ASCII (quebra de linha do '\n')
            //quando e salvo no arquivo. Esta verificacao faz o processo inverso
            //da conversao. Sem isto se o caractere 13 fosse um dos caracteres
            //da codificacao, ocorreria erro na decodificacao.
            if (simb2Bin(entrada.charAt(i)).equals("00001010") && entrada.substring(i+1,i+5).equals("<13>")){
                binario = binario + "00001101";
                i = i+4;
            }else{
                binario = binario + simb2Bin(entrada.charAt(i));
            }

        }        
        //remove os zeros do comeco da string, caso existam
        int x= 0;
        char z = ' ';
        String s = "";
        z = entrada.charAt(achaPos(entrada)-1);        
        s = s + z;        
        x = Integer.parseInt(s);
        binario = binario.substring(x,binario.length());
        System.out.println('\n' + "Codificacao binaria do texto: ");
        System.out.println(binario);
        System.out.println('\n' + "Aguarde um momento...");
        String saida_final = bin2Char(binario);
        System.out.println('\n' + "Texto decodificado: ");
        System.out.println(saida_final);
        System.out.println('\n' + "Decodificacao Concluida!");
        return saida_final;
    }

    //Retorna a posicao do fim da codificacao dos caracteres no arquivo,
    //delimitada por ":x:" (no caso a posicao do caracter ":" depois de "x"),
    //onde x = numero de 0's que foram adicionados para que os binarios
    //formassem cadeias de bytes (8 bits) completas
    private int achaPos(String entrada){
        int i = 0;
        for (i=0; i < entrada.length(); i++){
            if (entrada.charAt(i) == ':' && entrada.charAt(i+2) == ':') {
                return i+2;
            }
        }
        return 0;
    }
    //converte um char para uma string de binarios e retorna a mesma
    private String simb2Bin(char c){
        int dec = (int) c;
        String bin = "";
        bin = Integer.toBinaryString(dec);        
        if (bin.length() != 8){
            int qtd0 = 8 - (bin.length() % 8);
            for (int i=0; i<qtd0; i++){
                bin = "0" + bin;
            }
        }
        return bin;
    }

    //converte a string binaria para os caracteres baseados em seus codewords
    //i = numero de caracteres da codeword
    //j = indice do vetor de nos
    //bin = string binaria
    // traduzida = string que recebe os caracteres conforme estes vao sendo decodificados
    private String bin2Char(String bin){
        int i=0, j=0;
        String aux = "", traduzida = "";
        for(i=1;i<=bin.length();i++){
            aux = bin.substring(0,i);
            for (j=0;j<chars.size();j++){
                if (aux.equals(chars.get(j).getCod())){
                    traduzida = traduzida + chars.get(j).getSimb();
                    bin = bin.substring(i, bin.length());
                    j = chars.size();
                    i = 0;
                }
            }
        }
        return traduzida;
    }

}