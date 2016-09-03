/*
 * 
 * No.java
 * Author:
 * Renato Jensen Filho
 * 
 */

package br.com.jensen.beans;

public class No {

        private No esquerda;
        private No direita;
        private int freq;
        private char simb;
        private String cod;

        //construtor da classe No
        public No(int freq, char simb) {
            this.freq = freq;
            this.simb = simb;
            this.cod = null;
            this.esquerda = null;
            this.direita = null;
        }

        //Retorna o codeword do No
        public String getCod() {
            return cod;
        }

        //Atribui um valor ao codeword do No
        public void setCod(String cod) {
            this.cod = cod;
        }

        //Retorna a frequencia do No
        public int getFreq() {
            return freq;
        }

        //Atribui um valor a frequencia do No
        public void setFreq(int freq) {
            this.freq = freq;
        }

        //Retorna o simbolo (caractere) do No
        public char getSimb() {
            return simb;
        }

        //Atribui um valor ao simbolo (caractere) do No
        public void setSimb(char simb) {
            this.simb = simb;
        }

        //Retorna o No a direita do No
        public No getDireita() {
            return direita;
        }

        //Atribui um No a direita do No
        public void setDireita(No direita) {
            this.direita = direita;
        }

        //Retorna o No a esquerda do No
        public No getEsquerda() {
            return esquerda;
        }

        //Atribui um No a esquerda do No
        public void setEsquerda(No esquerda) {
            this.esquerda = esquerda;
        }

        //Construtor vazio da classe No
        public No(){
            this.esquerda = null;
            this.direita = null;
        }

        //Funcao recursiva que gera a codeword do No a partir da arvore de Huffman
        private void codificaNo(String path) {
            if ((esquerda == null) && (direita == null)) {
                cod = path;                
            }

            if (esquerda != null) {
                esquerda.codificaNo(path + '0');
            }
            if (direita != null) {
                direita.codificaNo(path + '1');
            }
        }

        //Funcao que recebe como parametro a raiz da arvore e inicia a codificacao a partir da mesma
        public static void geraCodificacao(No raiz) {
            raiz.codificaNo("");
        }

    }
