package com.tencent.tigermei.plugin

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal

class GreetingPlugin implements Plugin<ProjectInternal> {

    void testListAndMap(){
        //中括号[]用来定义List
        def list = ["Groovy", "Java", "Kotlin"]
        //groovy中的List默认是java中的ArrayList
        assert list instanceof ArrayList

        //运算符重载，向list中添加元素，也可以调用ArrayList的相关方法操作该list
        list << "Python"
        //通过下标访问
        assert list[1] == "Java"

        //遍历list可以使用each方法，传入一个闭包，闭包的参数为list的每个元素
        list.each {
            it -> println it
        }

        //当某一条件达到时需要终止遍历时，可以使用any方法。闭包返回true时，终止遍历。
        // 下列语句只会打印Groovy和Java
        list.any {
            println it
            return it == "Java"
        }

        //[:]用来定义Map，键值默认时String类型
        def map = [groovy:"Groovy", java:"Java", kotlin:"Kotlin"]
        //groovy中的Map默认是java中的LinkedHashMap
        assert map instanceof LinkedHashMap
        //可以通过点运算符和中括号来访问map中的元素
        assert map.java == "Java"
        assert map["groovy"] == "Groovy"

        //遍历map，闭包中的参数为Entry
        map.each { Map.Entry entry ->
            println "${entry.key}:${entry.value}"
        }

        map.any {
            entry ->
                println "${entry.key}:${entry.value}"
                return entry.value == "Java"
        }

    }

    testString(){
        //定义普通String的方法
        // 其中第二种允许换行
        def string = ['groovy', '''groovy''']
        string.each {
            assert it instanceof String
        }

        //定义允许插值GString的方法
        // 后面三种定义方式都允许换行
        def cool = "so cool!!"
        def gString = ["groovy ${cool}", """groovy ${cool}""", /groovy ${cool}/, $/groovy ${cool}/$]
        gString.each {
            assert it instanceof GString
            assert it.toString() == ('groovy so cool!!')
        }

        //若没有插值，则仍然为String类型
        def normalString = ["groovy", """groovy""", /groovy/, $/groovy/$]
        normalString.each {
            assert it instanceof String
        }

        //String和GString表示同一个字符串拥有不同的hashcode，所以不要用GString作为HashMap的键
        def groovy = 'groovy'
        assert 'groovy'.hashCode() != "${groovy}".hashCode()
    }

    testDelayString(){
        def language = "GROOVY"
        //插值是一个普通语句时，只在字符串在初始化时计算表达式

        def string = "${language.toLowerCase()} is cool"

        //插值是一个闭包时，每次引用该字符串都会执行闭包
        def delayString = "${-> language.toLowerCase()} is cool"

        assert string.toString() == "groovy is cool"
        assert delayString.toString() == "groovy is cool"

        //改变language值时，string值不会变化，delayString会变化
        language = "JAVA"

        assert string.toString() == "groovy is cool"
        assert delayString.toString() == "java is cool"
    }

    void testMacher(){
        def string = "G32ro3o2v6y"
        //定义正则表达式

        def pattern = /[^A-Za-z]/
        //计算匹配

        def matcher  = string =~ pattern

        //实际上是通过java的Matcher类进行匹配的
        assert matcher instanceof java.util.regex.Matcher
        assert matcher.replaceAll("") == "Groovy"
    }

    void apply(ProjectInternal project) {
//        project.task('hello') << {
//
//        }
        testListAndMap()
        testString()

        testDelayString()
        testMacher()

        println 'hello'
        println 'hello'
    }
}