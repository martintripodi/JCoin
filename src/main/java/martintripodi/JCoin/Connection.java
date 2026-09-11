package martintripodi.JCoin;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    // Json socket
    private static final ObjectMapper mapper = Mapper.createMapper();

    public MinableBlock getMinableBlock() {
        try {
            Socket socket = new Socket("localhost", 8080);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("get_minable_block");

            String json = reader.readLine();

            socket.close();

            return mapper.readValue(json, MinableBlock.class);

        } catch (Exception ex) {
            System.out.println("error");
            return null;
        }
    }


    public boolean verifyMinedBlock(long nonce, String address) {
        try {
            Socket socket = new Socket("localhost", 8080);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("verify_mined_block " + nonce + " " + address);

            String response = reader.readLine();

            socket.close();

            return Boolean.parseBoolean(response);

        } catch (Exception ex) {
            System.out.println("error");
            return false;
        }
    }


    public void pushBlock(Block block) {
        try {
            Socket socket = new Socket("localhost", 8080);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String json = mapper.writeValueAsString(block);

            writer.println("push_block " + json);

            String response = reader.readLine();

            socket.close();

        } catch (Exception ex) {
            System.out.println("error");
        }
    }


    public int getWalletCoins(String address) {
        try {
            Socket socket = new Socket("localhost", 8080);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("get_wallet_coins " + address);

            String response = reader.readLine();

            socket.close();

            return Integer.parseInt(response);

        } catch (Exception ex) {
            System.out.println("error");
            return 0;
        }
    }

}
