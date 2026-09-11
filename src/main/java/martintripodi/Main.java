package martintripodi;

import martintripodi.JCoin.*;

public class Main {
    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            Node node = new Node();
            node.startNode();
        });
        thread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Connection connection = new Connection();
        MinableBlock block = connection.getMinableBlock();

        Wallet myWallet = new Wallet();
        Miner miner = new Miner();
        long nonce = miner.mine(block);
        connection.verifyMinedBlock(nonce,myWallet.address);
        System.out.println(connection.getWalletCoins(myWallet.address));
    }
}