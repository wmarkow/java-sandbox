package jni.example;

public class JNIDemo
{

    // Declare a native method
    public native void sayHello();

    public native void fatalError();

    // Load the library that contains the native method implementation
    static
    {
        System.loadLibrary( "JNIDemo" );
    }

    public static void main( String[] args )
    {
        JNIDemo demo = new JNIDemo();

        if( args.length == 1 && "fatalError".equals( args[ 0 ] ) )
        {
            demo.fatalError();
            return;
        }

        demo.sayHello();
    }
}