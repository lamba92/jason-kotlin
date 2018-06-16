last_order_id(1, 5).
!start(smt).

// plan to achieve the goal "order" for agent Ag
+!order(Product,Qtd)[source(Ag)] :
    available_beers(K) & K>=Qtd
    <-
        .print("New order received. Qtd: ", Qtd);
        ?last_order_id(N, _);
        OrderId = N + 1;
        NewStock = K - Qtd;
        deliver(Product,Qtd);
        .send(Ag, tell, delivered(Product,Qtd,OrderId));
        .print("Order sent to ", Ag, ". New order ID was: ", OrderId);
        -+last_order_id(OrderId, Qtd);
        -+available_beers(NewStock).

+!order(Product,Qtd)[source(Ag)] :
    available_beers(K) & K<Qtd
    <-
        .print("Order qty: ", Qtd, " | Available qty: ", K, " | NOT ENOUGH BEER AVAILABLE SORRY");
        .send(Ag, tell, failed(Product,Qtd,OrderId)).

+!start(_)[source(self)] :
    true
    <-
        project.random_number(15, 3, Quantity);
        +available_beers(Quantity);
        .print("Beer stock refurbished. Qty: ", Quantity).

