import java.util.ArrayList;
import java.util.List;

public class ArvoreBinaria {
    private class No {
        Game jogo;
        No esquerda;
        No direita;
        
        public No(Game jogo) {
            this.jogo = jogo;
            this.esquerda = null;
            this.direita = null;
        }
    }
    
    private No raiz;
    
    public ArvoreBinaria() {
        this.raiz = null;
    }
    
    public void inserir(Game jogo) {
        raiz = inserirRecursivo(raiz, jogo);
    }
    
    private No inserirRecursivo(No no, Game jogo) {
        if (no == null) {
            return new No(jogo);
        }
        
        if (jogo.getName().compareTo(no.jogo.getName()) < 0) {
            no.esquerda = inserirRecursivo(no.esquerda, jogo);
        } else if (jogo.getName().compareTo(no.jogo.getName()) > 0) {
            no.direita = inserirRecursivo(no.direita, jogo);
        }
        
        return no;
    }
    
    public Game buscar(String nome) {
        return buscarRecursivo(raiz, nome);
    }
    
    private Game buscarRecursivo(No no, String nome) {
        if (no == null || no.jogo.getName().equals(nome)) {
            return no != null ? no.jogo : null;
        }
        
        if (nome.compareTo(no.jogo.getName()) < 0) {
            return buscarRecursivo(no.esquerda, nome);
        }
        
        return buscarRecursivo(no.direita, nome);
    }
    
    public Game[] toArray() {
        List<Game> lista = new ArrayList<>();
        percorrerEmOrdem(raiz, lista);
        return lista.toArray(new Game[0]);
    }
    
    private void percorrerEmOrdem(No no, List<Game> lista) {
        if (no != null) {
            percorrerEmOrdem(no.esquerda, lista);
            lista.add(no.jogo);
            percorrerEmOrdem(no.direita, lista);
        }
    }
} 