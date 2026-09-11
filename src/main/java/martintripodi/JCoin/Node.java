package martintripodi.JCoin;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Node {
    private static final ObjectMapper mapper = Mapper.createMapper();

    private static Network network;

    public Node(){
        network = new Network();
    }

    public void startNode(){
        try{
            ServerSocket serverSocket = new ServerSocket(8080);
            while(true){
                Socket socket = serverSocket.accept();

                // client connected
                handleClient(socket);
            }

        }catch(Exception ex){
            System.out.println("error starting node");
        }
    }

    private static void handleClient(Socket socket) {

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {

            String request;

            while ((request = reader.readLine()) != null) {
                if (request.equals("ping")) {

                    writer.println("pong");

                } else if (request.equals("get_minable_block")) {

                    writer.println(
                            mapper.writeValueAsString(network.getMinableBlock())
                    );
                }
                else if (request.startsWith("verify_mined_block")) {

                    String[] parts = request.split(" ", 3);

                    if (parts.length != 3) {
                        writer.println("missing nonce or address");
                    } else {
                        long nonce = Long.parseLong(parts[1]);
                        String address = parts[2];

                        boolean success =
                                network.verifyMinedBlock(nonce, address);

                        writer.println(success);
                    }
                }

                else if (request.startsWith("push_block")) {

                    String json = request.substring("push_block".length());

                    try {
                        Block block = mapper.readValue(json, Block.class);

                        network.pushBlock(block);

                        writer.println("success");

                    } catch (Exception e) {
                        writer.println("invalid block");
                    }
                }

                else if (request.startsWith("get_wallet_coins")) {

                    String[] parts = request.split(" ", 2);

                    if (parts.length != 2) {
                        writer.println("missing address");
                    } else {
                        String address = parts[1];

                        int coins = network.getWalletCoins(address);

                        writer.println(coins);
                    }

                }
            }

        } catch (Exception e) {
            System.out.println("error");
        } finally {

            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }
}
