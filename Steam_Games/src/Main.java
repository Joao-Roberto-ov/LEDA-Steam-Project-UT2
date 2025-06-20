import java.io.File;
import java.util.*;

public class Main {
    private static final String[] ATTRIBUTES = {
        "releaseDate", "price", "achievements"
    };
    private static final List<ResultadoOrdenacao> resultados = new ArrayList<>();
    private static ArvoreBinaria arvoreJogos;
    private static Fila filaProcessamento;

    public static void main(String[] args) {
        try {
            // Verificar se o arquivo games.csv existe
            File gamesFile = new File("../dados/games.csv");
            if (!gamesFile.exists()) {
                System.err.println("ERRO: O arquivo games.csv não foi encontrado na pasta dados!");
                System.err.println("Por favor, copie o arquivo games.csv da pasta database para a pasta dados.");
                return;
            }

            // Criar diretório de resultados se não existir
            File resultadosDir = new File("../dados/resultados");
            if (!resultadosDir.exists()) {
                resultadosDir.mkdirs();
            }

            System.out.println("Convertendo as datas...");
            ConversorData.main(args);

            // Verificar se o arquivo games_formated_release_data.csv foi criado
            File formattedFile = new File("../dados/games_formated_release_data.csv");
            if (!formattedFile.exists()) {
                System.err.println("ERRO: Falha ao criar o arquivo games_formated_release_data.csv");
                return;
            }

            System.out.println("Filtrando os jogos para Linux...");
            FiltroLinux.main(args);

            System.out.println("Filtrando jogos com suporte a português...");
            FiltroPortugues.main(args);

            System.out.println("Lendo dados dos jogos...");
            Game[] games = CSVManager.readGamesFromCSV("../dados/games_formated_release_data.csv");
            System.out.println("Total de jogos carregados: " + games.length);

            // Inicializar estruturas de dados
            arvoreJogos = new ArvoreBinaria();
            filaProcessamento = new Fila();

            // Carregar jogos na árvore e na fila
            for (Game game : games) {
                arvoreJogos.inserir(game);
                filaProcessamento.enfileirar(game);
            }

            // Processar jogos da fila
            while (!filaProcessamento.estaVazia()) {
                Game game = filaProcessamento.desenfileirar();
                // Aqui você pode adicionar lógica adicional de processamento se necessário
            }

            // Executar algoritmos de ordenação para cada atributo
            for (String attribute : ATTRIBUTES) {
                System.out.println("\nOrdenando por " + attribute + "...");
                
                // Preparar arrays para diferentes casos
                Game[] melhorCaso = Arrays.copyOf(games, games.length);
                Game[] medioCaso = Arrays.copyOf(games, games.length);
                Game[] piorCaso = Arrays.copyOf(games, games.length);
                
                // Ordenar para melhor caso
                Arrays.sort(melhorCaso, (a, b) -> {
                    if (attribute.equals("releaseDate")) {
                        return a.getReleaseDate().compareTo(b.getReleaseDate());
                    } else if (attribute.equals("price")) {
                        return Double.compare(a.getPrice(), b.getPrice());
                    } else {
                        return Integer.compare(a.getAchievements(), b.getAchievements());
                    }
                });
                
                // Embaralhar para caso médio
                List<Game> lista = Arrays.asList(medioCaso);
                Collections.shuffle(lista);
                lista.toArray(medioCaso);
                
                // Ordenar reversamente para pior caso
                Arrays.sort(piorCaso, (a, b) -> {
                    if (attribute.equals("releaseDate")) {
                        return b.getReleaseDate().compareTo(a.getReleaseDate());
                    } else if (attribute.equals("price")) {
                        return Double.compare(b.getPrice(), a.getPrice());
                    } else {
                        return Integer.compare(b.getAchievements(), a.getAchievements());
                    }
                });
                
                // Executar algoritmos de ordenação
                executarOrdenacao("Selection Sort", melhorCaso, medioCaso, piorCaso, attribute);
                executarOrdenacao("Insertion Sort", melhorCaso, medioCaso, piorCaso, attribute);
                executarOrdenacao("Merge Sort", melhorCaso, medioCaso, piorCaso, attribute);
                executarOrdenacao("Quick Sort", melhorCaso, medioCaso, piorCaso, attribute);
                executarOrdenacao("Quick Sort M3", melhorCaso, medioCaso, piorCaso, attribute);
                executarOrdenacao("Heap Sort", melhorCaso, medioCaso, piorCaso, attribute);
                
                // Counting Sort apenas para achievements
                if (attribute.equals("achievements")) {
                    executarOrdenacao("Counting Sort", melhorCaso, medioCaso, piorCaso, attribute);
                }
            }

            // Imprimir tabela de resultados
            imprimirTabelaResultados();

            System.out.println("\nProcessamento concluído com sucesso!");
            System.out.println("Os resultados foram salvos na pasta ../dados/resultados/");

        } catch (Exception e) {
            System.err.println("Erro durante a execução: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executarOrdenacao(String algoritmo, Game[] melhorCaso, Game[] medioCaso, Game[] piorCaso, String attribute) {
        try {
            // Melhor caso
            Game[] melhorCasoCopy = Arrays.copyOf(melhorCaso, melhorCaso.length);
            long memoriaInicial = getMemoriaUtilizada();
            long startTime = System.currentTimeMillis();
            executarAlgoritmo(algoritmo, melhorCasoCopy, attribute);
            long endTime = System.currentTimeMillis();
            long memoriaFinal = getMemoriaUtilizada();
            long memoriaUtilizada = Math.abs(memoriaFinal - memoriaInicial);
            
            resultados.add(new ResultadoOrdenacao(algoritmo, attribute, "Melhor", endTime - startTime, memoriaUtilizada));
            CSVManager.writeGamesToCSV(melhorCasoCopy, "../dados/resultados/games_" + attribute + "_" + algoritmo.replace(" ", "") + "_melhorCaso.csv");

            // Caso médio
            Game[] medioCasoCopy = Arrays.copyOf(medioCaso, medioCaso.length);
            memoriaInicial = getMemoriaUtilizada();
            startTime = System.currentTimeMillis();
            executarAlgoritmo(algoritmo, medioCasoCopy, attribute);
            endTime = System.currentTimeMillis();
            memoriaFinal = getMemoriaUtilizada();
            memoriaUtilizada = Math.abs(memoriaFinal - memoriaInicial);
            
            resultados.add(new ResultadoOrdenacao(algoritmo, attribute, "Médio", endTime - startTime, memoriaUtilizada));
            CSVManager.writeGamesToCSV(medioCasoCopy, "../dados/resultados/games_" + attribute + "_" + algoritmo.replace(" ", "") + "_medioCaso.csv");

            // Pior caso
            Game[] piorCasoCopy = Arrays.copyOf(piorCaso, piorCaso.length);
            memoriaInicial = getMemoriaUtilizada();
            startTime = System.currentTimeMillis();
            executarAlgoritmo(algoritmo, piorCasoCopy, attribute);
            endTime = System.currentTimeMillis();
            memoriaFinal = getMemoriaUtilizada();
            memoriaUtilizada = Math.abs(memoriaFinal - memoriaInicial);
            
            resultados.add(new ResultadoOrdenacao(algoritmo, attribute, "Pior", endTime - startTime, memoriaUtilizada));
            CSVManager.writeGamesToCSV(piorCasoCopy, "../dados/resultados/games_" + attribute + "_" + algoritmo.replace(" ", "") + "_piorCaso.csv");

        } catch (Exception e) {
            System.err.println("Erro ao executar " + algoritmo + " para " + attribute + ": " + e.getMessage());
        }
    }

    private static void executarAlgoritmo(String algoritmo, Game[] games, String attribute) {
        switch (algoritmo) {
            case "Selection Sort":
                SortingAlgorithms.selectionSort(games, attribute);
                break;
            case "Insertion Sort":
                SortingAlgorithms.insertionSort(games, attribute);
                break;
            case "Merge Sort":
                SortingAlgorithms.mergeSort(games, attribute);
                break;
            case "Quick Sort":
                SortingAlgorithms.quickSort(games, attribute);
                break;
            case "Quick Sort M3":
                SortingAlgorithms.quickSortMedian3(games, attribute);
                break;
            case "Heap Sort":
                SortingAlgorithms.heapSort(games, attribute);
                break;
            case "Counting Sort":
                SortingAlgorithms.countingSort(games, attribute);
                break;
        }
    }

    private static long getMemoriaUtilizada() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static void imprimirTabelaResultados() {
        System.out.println("\n=== RESULTADOS DAS ORDENAÇÕES ===");
        System.out.println("================================================================");
        System.out.println("Algoritmo        | Atributo     | Caso      | Tempo (ms) | Memória (KB)");
        System.out.println("================================================================");

        // Ordenar resultados por atributo e algoritmo
        resultados.sort((a, b) -> {
            int compAtributo = a.getAtributo().compareTo(b.getAtributo());
            if (compAtributo != 0) return compAtributo;
            int compAlgoritmo = a.getAlgoritmo().compareTo(b.getAlgoritmo());
            if (compAlgoritmo != 0) return compAlgoritmo;
            return a.getCaso().compareTo(b.getCaso());
        });

        for (ResultadoOrdenacao resultado : resultados) {
            System.out.println(resultado);
        }
        System.out.println("================================================================");
    }
}