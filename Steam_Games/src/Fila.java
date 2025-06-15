public class Fila {
    private class No {
        Game jogo;
        No proximo;
        
        public No(Game jogo) {
            this.jogo = jogo;
            this.proximo = null;
        }
    }
    
    private No inicio;
    private No fim;
    private int tamanho;
    
    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }
    
    public void enfileirar(Game jogo) {
        No novoNo = new No(jogo);
        if (estaVazia()) {
            inicio = novoNo;
        } else {
            fim.proximo = novoNo;
        }
        fim = novoNo;
        tamanho++;
    }
    
    public Game desenfileirar() {
        if (estaVazia()) {
            return null;
        }
        
        Game jogo = inicio.jogo;
        inicio = inicio.proximo;
        tamanho--;
        
        if (estaVazia()) {
            fim = null;
        }
        
        return jogo;
    }
    
    public Game primeiro() {
        return estaVazia() ? null : inicio.jogo;
    }
    
    public int getTamanho() {
        return tamanho;
    }
    
    public boolean estaVazia() {
        return tamanho == 0;
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
} 