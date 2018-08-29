# Socket Pool

It's an small artifact (library) to help creating a Socket pool. It's basicly and implementations of the on _apache commons pool 2_ 
for Socket, plus a Helper class that allow initialize or get instance of a Socket Pool. 

## How to use it?
You can initialize a new pool by using:

    SocketHelper helper = SocketHelper.getInstance("127.0.0.1", 800);

The below line is going to initilizate a new pool or return and existent pool.
Then you can use the methods of the helper to perform actions on the pool. 

### Getting a new Socket Client from the pool. 
When you want to interact with a Socket server endpoint you will use a SocketClient object from the pool. 
To obtaing one of them you just need to use: 

    SocketClient client = helper.getSocket();
    
    byte [] messageInput = ...;
    byte [] messageOutput = client.execute(messageInput);
    

 ### Returning a Socket Client to the pool
 Whe you finished using a Socket client instance you can return it back to the pool by using

    helper.return(client);
    
## Shutting down a Socket Pool 
When you decided to shutdown a pool just use the following line: 

    helper.shutdown();

That line is going to shutdown the pool that was initiliaze or gotten on that instance.

## Customizing Configurations
This library allows to use custom configurations on the create pool. That's done by using the GenericObjectPoolConfig class from Apache Commons Pool2. 

    helper.setConfiguration(GenericObjectPoolConfig config);
    
For example, it's possible to customize the minimum, maximum, initial amount of instances on the pool. 


