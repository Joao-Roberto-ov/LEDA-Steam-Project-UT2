public class ListaEncadeada {
    private class No {
        Game jogo;
        No proximo;
        
        public No(Game jogo) {
            this.jogo = jogo;
            this.proximo = null;
        }
    }
    
    private No inicio;
    private int tamanho;
    
    public ListaEncadeada() {
        this.inicio = null;
        this.tamanho = 0;
    }
    
    public void adicionar(Game jogo) {
        No novoNo = new No(jogo);
        if (inicio == null) {
            inicio = novoNo;
        } else {
            No atual = inicio;
            while (atual.proximo != null) {
                atual = atual.proximo;
            }
            atual.proximo = novoNo;
        }
        tamanho++;
    }
    
    public Game[] toArray() {
        Game[] array = new Game[tamanho];
        No atual = inicio;
        int i = 0;
        while (atual != null) {
            array[i++] = atual.jogo;
            atual = atual.proximo;
        }
        return array;
    }
    
    public int getTamanho() {
        return tamanho;
    }
    
    public boolean estaVazia() {
        return tamanho == 0;
    }
} 