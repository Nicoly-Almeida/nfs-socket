package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class Servidor2 {
    static String arquivos = System.getProperty("user.dir") + "/arquivos";

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor NFS ==");

        ServerSocket serverSocket = new ServerSocket(1025);
        Socket socket = serverSocket.accept();
        System.out.println("Cliente conectado");

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        Path diretorioArquivos = Paths.get(arquivos);
        if (!Files.exists(diretorioArquivos)) {
            Files.createDirectories(diretorioArquivos);
        }

        while (true) {
            String mensagem = dis.readUTF();

            if (mensagem.startsWith("readdir")) {
                List<String> listaArquivos = listarArquivos();
                StringBuilder listagem = new StringBuilder();

                for (String arquivo : listaArquivos) {
                    listagem.append(arquivo).append(" ");
                }
                dos.writeUTF(listagem.toString());
            } else if (mensagem.startsWith("rename")) {
                String[] partes = mensagem.split(" ");
                if (partes.length == 3) {
                    String antigoNome = partes[1];
                    String novoNome = partes[2];
                    renameArquivo(antigoNome, novoNome);
                }
            } else if (mensagem.startsWith("create")) {
                String nomeArquivo = mensagem.split(" ")[1];
                createArquivo(nomeArquivo);
            } else if (mensagem.startsWith("remove")) {
                String nomeArquivo = mensagem.split(" ")[1];
                removeArquivo(nomeArquivo);
            }
        }
    }

    // Métodos para operações no sistema de arquivos
    private static List<String> listarArquivos() throws IOException {
        Stream<Path> files = Files.list(Paths.get(arquivos));
        List<String> filesString = new ArrayList<>();
        for (Path file : files.toList()) {
            filesString.add(file.getFileName().toString());
        }
        return filesString;
    }

    private static void renameArquivo(String antigoNome, String novoNome) throws IOException {
        Path file = Paths.get(arquivos + "/" + antigoNome);
        Files.move(file, file.resolveSibling(novoNome));
    }

    private static void createArquivo(String nomeArquivo) throws IOException {
        System.out.println("teste");
        Path file = Paths.get(arquivos + "/" + nomeArquivo);
        Files.createFile(file);
    }

    private static void removeArquivo(String nomeArquivo) throws IOException {
        Path file = Paths.get(arquivos + "/" + nomeArquivo);
        Files.delete(file);
    }
}
