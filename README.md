# JCoin

JCoin is a **Java-based, server-authoritative cryptocurrency system** featuring an in-memory blockchain, cryptographic hashing, wallet management, and block mining.

Each JCoin **node** is an independently operated server containing its own `Network` instance. The `Network` maintains the complete blockchain in memory and is responsible for validating blocks, processing the chain, and deriving wallet balances from its blockchain state.

Clients communicate with nodes over **TCP sockets** to request minable blocks, submit mining results, and push blocks to the network. Mining provides the mechanism for generating coins, while the node's `Network` remains the authoritative source of the blockchain and all derived wallet state.

Nodes do not communicate or synchronize with one another. Each node therefore operates as an **independent JCoin network** with its own blockchain and state.
