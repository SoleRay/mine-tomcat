package connector.endpoint;

import sun.nio.ch.SelectionKeyImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NioEndpoint extends AbstractJsseEndpoint<SocketChannel> {


    private volatile ServerSocketChannel serverSock;

    @Override
    public void bind() throws Exception {
        initServerSocket();
    }

    @Override
    public void unbind() throws Exception {

    }

    @Override
    public void startInternal() throws Exception {
        startAcceptorThreads();
    }

    @Override
    public void stopInternal() throws Exception {

    }

    private void initServerSocket() throws Exception {
        serverSock = ServerSocketChannel.open();
        socketProperties.setProperties(serverSock.socket());
        InetSocketAddress addr = (getAddress()!=null?new InetSocketAddress(getAddress(),getPort()):new InetSocketAddress(getPort()));
        serverSock.socket().bind(addr,getAcceptCount());
        serverSock.configureBlocking(false);
    }



    private String getResponseText() {
        StringBuffer sb = new StringBuffer();
        sb.append("HTTP/1.1 200 OK\n");
        sb.append("Content-Type: text/html; charset=UTF-8\n");
        sb.append("\n");
        sb.append("<html>");
        sb.append("  <head>");
        sb.append("    <title>");
        sb.append("      NIO Http Server");
        sb.append("    </title>");
        sb.append("  </head>");
        sb.append("  <body>");
        sb.append("    <h1>Hello World!</h1>");
        sb.append("  </body>");
        sb.append("</html>");

        return sb.toString();
    }


    @Override
    protected SocketChannel serverSocketAccept() throws Exception {
        return serverSock.accept();
    }

    @Override
    protected boolean setSocketOptions(SocketChannel channel) {
        return false;
    }

    @Override
    protected void closeSocket(SocketChannel channel) {
        try {
            channel.socket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void process() throws Exception {
        Selector selector = Selector.open();
        serverSock.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select(1000);
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isAcceptable()){
                    accept(selector, key);
                }else if(key.isReadable()){
                    read(key);
                }else if(key.isWritable()){
                    write(key);
                }

            }

        }
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector,SelectionKey.OP_READ);
        key.interestOps(SelectionKey.OP_ACCEPT);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        StringBuilder sb = new StringBuilder();
        while (channel.read(byteBuffer)>0){
            byteBuffer.flip();
            CharBuffer cb = Charset.forName("UTF-8").decode(byteBuffer);
            sb.append(cb.toString());
        }

        System.out.println(sb);

        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        String responseText = getResponseText();
        ByteBuffer byteBuffer = ByteBuffer.wrap(responseText.getBytes());

        while (byteBuffer.hasRemaining()){
            channel.write(byteBuffer);
        }
        channel.close();
    }
}
