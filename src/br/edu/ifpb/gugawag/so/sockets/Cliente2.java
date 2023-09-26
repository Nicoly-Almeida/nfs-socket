package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Cliente2 {

    public static void main(String[] args) throws IOException {
        System.out.println("== Cliente NFS ==");

        Socket socket = new Socket("127.0.0.1", 1025);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DataInputStream dis = new DataInputStream(socket.getInputStream());


        while (true) {
            Scanner teclado = new Scanner(System.in);
            String comando = teclado.nextLine();

            dos.writeUTF(comando);

            String resposta = dis.readUTF();
            System.out.println("Servidor respondeu: " + resposta);
        }
    }
}
