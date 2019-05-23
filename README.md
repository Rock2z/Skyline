# Skyline
An algorithm used to draw the skyline from an existing image.

最终希望实现类似这样的效果（图片网上随便找的，版权问题请联系我）：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190523084107118.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMzOTgyMjMy,size_16,color_FFFFFF,t_70)


获取天际线的流程大概是这样：

1.读取图片，获取rgb值存入数组
2.根据提取出蓝色通道的值，并且绘制出直方图
3.根据直方图算出一个阈值threshold
4.把图中所有蓝色通道的值高于threshold的点全部染白（认为是天空）
把图中所有蓝色通道的值低于threshold的点全部染黑（认为是陆地）
5.从上往下遍历图片，保留第一个黑点，这样将得到一条黑线，就是天际线
6.连接天际线中不连续的点，最终得到一条完整的天际线

大概解释一下这个流程：
首先，我们对于每个像素pixel会得到一个rgb的值，比如（10,124,255）第三位是blue的值，这个值是在0-255之间的，那么我们统计所有像素点的rgb中的blue的值， 会得到很多0-255的值，把这些图绘制成一个直方图，这个直方图可能会有2个峰值，一个比较蓝，一个比较不蓝（好奇怪的表述。。），然后那个比较蓝的大概就是天空的颜色，那个比较不蓝的就是地面啦，那么这两个峰值中间的最小值就是我们所要的threshold了。
后面的部分好像都挺简单的~


做到第四步大概是这样的：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190523085130207.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMzOTgyMjMy,size_16,color_FFFFFF,t_70)










其中的关键点有：
1. 第三部中计算threshold的时候， 要先算出直方图的两个peak峰值，然后在这两个peak中间找到一个minimum value作为threshold，否则可能threshold会出现在peak外的位置；
2. 第6步中，我第一次写的时候遇到了一个小坑，图中做到第5步可能是下面这样的情况：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190523084441573.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMzOTgyMjMy,size_16,color_FFFFFF,t_70)
这是还没有连线的时候，看似有些x坐标上是没有黑点的，但其实每个x坐标上都是有黑点的，只是他们可能在一些奇怪的位置，这些点我们可以认为是无效点，这些点需要被我们忽略掉，然后得到一个离散的点图，然后把离散的点图之间用连线的方式连起来（这里连线的方式我采用的是一个比较笨的方法，get两个点，直接贪心遍历周围8个点去找最短距离作为next step，然后把所有的next step连起来）


就这样~
