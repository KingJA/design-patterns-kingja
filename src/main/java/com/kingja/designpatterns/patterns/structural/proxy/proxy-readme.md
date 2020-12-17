
### 代理模式

# 开篇
---
这篇文章是我参考资料并结合自己的理解的产物，算是对知识的总结，认知有限，如有错漏，欢迎讨论。

>**代理模式（Proxy）** 为对象提供一个代理，由代理对象**控制**对原对象的**访问**，可实现对原对象的方法功能**增强**

代理模式根据用途有很多种形式，但是在代码结构上，主要分2种代理，如下：


* 静态代理：为不同的目标类创建不同的代理类，生成不同的字节码文件，为不同的代理类不同的方法进行代理，代理逻辑写死在各自的类中。
* 动态代理：系统在运行的时候为目标对象动态创建代理类，可由一个代理类代理不同的目标类，不同的方法。

如果你能理解Web项目的静态页面和动态页面，那么就应该很容易理解静态代理和动态代理。

静态代理
---

首先我们实现一个简单的**静态代理**的demo
A公司开发了一个翻译软件，可以随身携带，翻译单词，他们先开发了英语翻译引擎
```java
public interface ITranslator {
    /**
     *
     * @param word 需要翻译的单词
     * @return boolean 是否翻译成功
     */
    public boolean translateWord(String word);
}
```

```java
public class EnglishTranslator implements ITranslator {
    @Override
    public boolean translateWord(String word) {
        System.out.println("【英语翻译引擎】 翻译单词：" + word);
        return false;
    }
}
```
投入市场一段时候，市场反应热烈，这时候该考虑回报了，于是顺势推出会员服务，加入了收费逻辑，但是不可能加在翻译引擎里，于是按静态代理的方式实现，分下面3步走：

* 1.代理类和目标类实现相同的接口
* 2.通过构造函数传入目标对象
* 3.实现和目标类相同的方法，并在原对象方法前后加入特殊逻辑

```java
public class EnglishTranslatorStaticProxy implements ITranslator{
    private ITranslator translator;

    public EnglishTranslatorStaticProxy(ITranslator translator) {
        this.translator = translator;
    }
    
    @Override
    public boolean translateWord(String word) {
        System.out.println("【VIP】 验证用户权限->通过");
        translator.translateWord(word);
        System.out.println("【VIP】 进行计费 ¥");
        return true;
    }
}
```
测试
```java
public class ProxyTest {
    public static void main(String[] args) {
        /*=========静态代理=========*/
        EnglishTranslatorStaticProxy englishTranslatorStaticProxy =
                new EnglishTranslatorStaticProxy(new EnglishTranslator());
        englishTranslatorStaticProxy.translateWord("梦想");
    }
}
```
```shell
//log
【VIP】 验证用户权限->通过
【英语翻译引擎】 翻译单词：梦想
【VIP】 进行计费 ¥
```
到目前为止貌似没大毛病，收费逻辑没入侵翻译模块。
随着看书学英语的人多起来，于是A公司又推出了翻译文章的功能，当然，也需要收费。于是我们的代码就变成了了这样：
```java
public class EnglishTranslatorStaticProxy implements ITranslator{
    private ITranslator translator;

    public EnglishTranslatorStaticProxy(ITranslator translator) {
        this.translator = translator;
    }

    @Override
    public boolean translateArticle(String article) {
        System.out.println("【VIP】 验证用户权限->通过");
        translator.translateArticle(article);
        System.out.println("【VIP】 进行计费 ¥");
        return true;
    }

    @Override
    public boolean translateWord(String word) {
        System.out.println("【VIP】 验证用户权限->通过");
        translator.translateWord(word);
        System.out.println("【VIP】 进行计费 ¥");
        return true;
    }
}
```


技术总监一看，这不行啊，每加入一个翻译功能都要加入相同的收费逻辑，而且都是重复的，不合理啊！！！
还没完呢，图文翻译组的也叫了，文本翻译都收费了，我们图文翻译都AI技术了，不收费说不过去吧，于是吵吵着也要收费，于是：

```java
public class EnglishImageTranslatorStaticProxy implements IImageTranslator{
    private IImageTranslator translator;

    public EnglishImageTranslatorStaticProxy(IImageTranslator translator) {
        this.translator = translator;
    }

    @Override
    public boolean translateImage(byte res) {
        System.out.println("【VIP】 验证用户权限->通过");
        translator.translateImage(res);
        System.out.println("【VIP】 进行计费 ¥");
        return false;
    }
}
```
大家发现没，不同接口需要不同的代理类，要靠增加代理类来实现不同接口类的代理，这就是静态代理的弊端。并且每个代理类的方法都要写重复的代码。
有没一种机制，不同接口，不同的方法都能使用同一个代理对象？
~~~~~~他来了他来了~~~~~他脚踏祥云进来了~~~~~(手动BGM)
动态代理
---

为什么叫动态代理,因为代理类是在运行时候才创建的

JDK1.3开始 java.lang.reflect中提供动态代理的实现
主要涉及两个类：
##### Proxy
动态代理的管理类，提供静态方法newProxyInstance(ClassLoader loader,Class<?>[]interfaces,InvocationHandler h)来生成代理类

* loader：代理类的类加载器
* interfaces：代理类所实现的接口列表
* h：代理处理类
##### InvocationHandler
作为代理类的处理器接口，接口方法Object invoke(Object proxy, Method method, Object[]args)来处理代理类的逻辑，并且执行被代理类的对应方法，代理类会把方法的执行转交给它执行

* proxy：代理类对象
* method：代理的方法
* args：代理方法的参数列表

动态代理的实现过程很简单，就2步：
* 1.创建类实现InvocationHandler接口，并在invoke方法中加入代理逻辑
* 2.通过Proxy的newProxyInstance创建代理对象

不逼逼了，直接上代码
接下来我们使用动态代理来实现静态代理相同的功能，在单词翻译，文章翻译，图文翻译都加入收费功能。
```java
public class EnglishTranslatorDynamicProxy implements InvocationHandler {
    /*被代理对象*/
    private Object targetObject;

    public EnglishTranslatorDynamicProxy(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //原方法执行前执行
        preInvoke();
        //原方法执行
        Object result = method.invoke(targetObject, args);
        //原方法执行后执行
        postInvoke();
        return result;
    }

    private void preInvoke() {
        System.out.println("【VIP】 验证用户权限->通过");
    }

    private void postInvoke() {
        System.out.println("【VIP】 进行计费 ¥");
    }
    
    /**
     * 生成代理对象
     * @return 代理对象
     */
    public Object bind() {
        Class<?> clazz = targetObject.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }
}
```
```java
public class ProxyTest {
    public static void main(String[] args) {
        /*=========动态代理=========*/
        ITranslator englishTranslatorDynamicProxy =
                (ITranslator) new EnglishTranslatorDynamicProxy(new EnglishTranslator()).bind();
        englishTranslatorDynamicProxy.translateArticle("《我有一个梦想》");
        englishTranslatorDynamicProxy.translateWord("梦想");

        IImageTranslator englishImageTranslatorDynamicProxy =
                (IImageTranslator) new EnglishTranslatorDynamicProxy(new EnglishImageTranslator()).bind();
        englishImageTranslatorDynamicProxy.translateImage(new byte[]{});

    }
}
```
```shell
//log
【VIP】 验证用户权限->通过
【英语翻译引擎】 翻译文章：《我有一个梦想》
【VIP】 进行计费 ¥

【VIP】 验证用户权限->通过
【英语翻译引擎】 翻译单词：梦想
【VIP】 进行计费 ¥

【VIP】 验证用户权限->通过
【英语图文翻译引擎】 翻译图文
【VIP】 进行计费 ¥
```
通过动态代理我们只用一个代理类实现了对2个目标类的3个方法的增强，我们没有动过2个目标类一行代码，仅仅是在动态代理的处理类中加入了增强代码，符合开闭原则，实现了目标类原方法逻辑和代理类增强方法逻辑的解耦。
为什么动态代理能在运行时动态生成代理类，为什么1个类能代理多个目标类，多个方法？
》》》》》》上源码！！！》》》》》》
对，不要怂，就是干。既然我们的代理对象是从Proxy的newProxyInstance方法获取，那么我们直接进入这个方法。
我们凭直觉，通过反射创建对象分3步：

- [ ] 1.获取代的Class对象
- [ ] 2.通过Class对象获取构造函数
- [ ] 3.通过构造函数获取实例对象


```java
    @CallerSensitive
    public static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,
    InvocationHandler h) {
        Objects.requireNonNull(h);
        final Class<?> caller = System.getSecurityManager() == null? null: Reflection.getCallerClass();
        //返回接受InvocationHandler类型的单个参数的代理类的构造函数对象
        //进入>>>>>>Step into
        Constructor<?> cons = getProxyConstructor(caller, loader, interfaces);
        return newProxyInstance(caller, cons, h);
    }
```
>给定类装入器和接口数组，返回接受InvocationHandler类型的单个参数的代理类的构造函数对象。返回的构造函数将已经设置了accessible标志。
```java
    private static Constructor<?> getProxyConstructor(Class<?> caller,ClassLoader loader,
                                                      Class<?>... interfaces{
        // 代理对象只实现1个接口
        if (interfaces.length == 1) {
            Class<?> intf = interfaces[0];
            if (caller != null) {
                checkProxyAccess(caller, loader, intf);
            }
            return proxyCache.sub(intf).computeIfAbsent(
                loader,
                (ld, clv) -> new ProxyBuilder(ld, clv.key()).build()
            );
            //代理对象实现多个接口 
        } else {
            final Class<?>[] intfsArray = interfaces.clone();
            if (caller != null) {
                checkProxyAccess(caller, loader, intfsArray);
            }
            final List<Class<?>> intfs = Arrays.asList(intfsArray);
            //进入build()>>>>>>Step into
            return proxyCache.sub(intfs).computeIfAbsent(
                loader,
                (ld, clv) -> new ProxyBuilder(ld, clv.key()).build()
            );
        }
    }
```
可以看出代理接口实现1个接口和实现多个接口，总的代理逻辑是一致的，最后并入proxyCache.sub(intfs).computeIfAbsent(loader,(ld, clv) -> new ProxyBuilder(ld, clv.key()).build());方法，根据类名我们可以看出ProxyBuilder是一个代理类的构造器，通过建造者模式创建代理对象，我们直接进入build()方法
>生成一个代理类，并返回已经设置了accessible标志的代理构造函数。如果目标模块没有访问任何接口类型的权限，那么IllegalAccessError将被VM在defineClass时抛出。在调用此方法之前，必须调用checkProxyAccess方法来执行权限检查。。
```java
Constructor<?> build() {
    //获取到Class对象
                          //进入>>>>>>Step into
    Class<?> proxyClass = defineProxyClass(module, interfaces);
    final Constructor<?> cons;
    try {
        //获取到带InvocationHandler参数的构造函数
        cons = proxyClass.getConstructor(constructorParams);
    } catch (NoSuchMethodException e) {
        throw new InternalError(e.toString(), e);
    }
    AccessController.doPrivileged(new PrivilegedAction<Void>() {
        public Void run() {
            //设置构造函数可访问
            cons.setAccessible(true);
            return null;
        }
    });
    return cons;
}
```
进入defineProxyClass方法

```java
private static Class<?> defineProxyClass(Module m, List<Class<?>> interfaces) {
   ...省略包检查等代码
    long num = nextUniqueNumber.getAndIncrement();
    //动态代理类的命名规则$Proxy0...nums
    String proxyName = proxyPkg.isEmpty()? proxyClassNamePrefix + num
                            : proxyPkg + "." + proxyClassNamePrefix + num;
    ClassLoader loader = getLoader(m);
    trace(proxyName, m, loader, interfaces);
    byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
            proxyName, interfaces.toArray(EMPTY_CLASS_ARRAY), accessFlags);
    try {
        //生成proxy对象的Class对象并加入到内存中
        Class<?> pc = UNSAFE.defineClass(proxyName, proxyClassFile,0, proxyClassFile.length,
                                         loader, null);
        reverseProxyCache.sub(pc).putIfAbsent(loader, Boolean.TRUE);
        return pc;
    } catch (ClassFormatError e) {
        throw new IllegalArgumentException(e.toString());
    }
}
```

主要的逻辑是在内存中生成了Class文件，命名规则是从$Proxy+num.class，比如$Proxy0.class

- [x] 1.获取代的Class对象
- [x] 2.通过Class对象获取构造函数
- [ ] 3.通过构造函数获取实例对象

自此我们实现了3步中的2步，只剩最后一步，创建构造函数，回到最开始的newProxyInstance方法，进入return 后面的另一个重载方法newProxyInstance并进入

```java
    @CallerSensitive
    public static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,
    InvocationHandler h) {
        Objects.requireNonNull(h);
        final Class<?> caller = System.getSecurityManager() == null? null: Reflection.getCallerClass();
        //返回接受InvocationHandler类型的单个参数的代理类的构造函数对象
        //进入>>>>>>Step into
        Constructor<?> cons = getProxyConstructor(caller, loader, interfaces);
               //进入>>>>>>Step into
        return newProxyInstance(caller, cons, h);
    }
```

```java
    private static Object newProxyInstance(Class<?> caller, // null if no SecurityManager
                                           Constructor<?> cons,InvocationHandler h) {
        try {
            if (caller != null) {
                checkNewProxyPermission(caller, cons.getDeclaringClass());
            }
            //通过带InvocationHandler参数的构造方法创建代理对象
            return cons.newInstance(new Object[]{h});
        } catch (IllegalAccessException | InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new InternalError(t.toString(), t);
            }
        }
    }
```


- [x] 1.获取代的Class对象
- [x] 2.通过Class对象获取构造函数
- [x] 3.通过构造函数获取实例对象
我们3个步骤都已经完成，那么是是否解开代理模式神秘面纱的时候了，我们记得系统给我生成了代理类的Class字节码文件并保存在内存中，那么我们可以把它保存到本地。
加入VM运行参数***-Djdk.proxy.ProxyGenerator.saveGeneratedFiles=true***重新运行程序，系统将在项目根目录下生成com.sun.proxy包，下面包含了所有的代理类文件，这里分别是ITranslator和IImageTranslator实现类的代理类

我们打开$Proxy0.class
```java
public final class $Proxy0 extends Proxy implements ITranslator {
    private static Method m1;
    private static Method m2;
    private static Method m4;
    private static Method m0;
    private static Method m3;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final boolean translateWord(String var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m4, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final int hashCode() throws  {
        try {
            return (Integer)super.h.invoke(this, m0, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final boolean translateArticle(String var1) throws  {
        try {
            return (Boolean)super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m4 = Class.forName("com.kingja.designpatterns.patterns.structural.proxy.ITranslator").getMethod("translateWord", Class.forName("java.lang.String"));
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
            m3 = Class.forName("com.kingja.designpatterns.patterns.structural.proxy.ITranslator").getMethod("translateArticle", Class.forName("java.lang.String"));
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
```

1.可以看出动态代理类，在被加载的时候通过反射获取接口下的所有方法
2.动态代理类执行方法的时候，调用InvocationHandler执行对应的逻辑

现在我们所有的线索都出现了，是时候把所有的拼图完整地呈现在大家面前



### 注意点

* 被代理的对象必须实现1个或1个以上接口
* 不同的接口不可以有名字和参数完全一样的方法
* 接口如果不是public，就必须属于同一个package

总结
---
静态代理只能通过增加代理类，来实现不同类的代理，显示的实现代理逻辑，耦合性强，代码复用度不高。
动态代理可以代理多个不同的类，可以代理不同的方法，无需创建多个代理类，具体实现为实现不同的代理处理类
符合开闭原则，实现了系统解耦。

参考
---
* JDK 11源码
* 《Head First》2007年9月第1版
