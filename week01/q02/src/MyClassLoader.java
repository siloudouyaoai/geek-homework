import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {


    public static final void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Class helloClass = new MyClassLoader().findClass("Hello");
        Method helloMethod = helloClass.getDeclaredMethod("hello", null);
        helloMethod.invoke(helloClass.newInstance(), null);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try (InputStream inputStream = MyClassLoader.getSystemResourceAsStream("Hello.xlass")) {
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            int realLength = 0;
            int length = bytes.length;
            for (; realLength < length; realLength++) {
                //byte 范围-128 - 127，用255减，不可能为0，遇到0结束
                if (bytes[realLength] == 0) {
                    break;
                }
                bytes[realLength] = (byte) (255 - bytes[realLength]);
            }
            byte[] loadBytes = new byte[realLength];
            System.arraycopy(bytes, 0, loadBytes, 0, realLength);
            return defineClass(name, loadBytes, 0, realLength);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
