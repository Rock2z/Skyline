import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ImgHandler {

    public void solve(String path){
        try{
            BufferedImage bimg = ImageIO.read(getClass().getResource(path));
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            System.out.println("w: "+width);
            System.out.println("h: "+height);
            int [][] data = new int[width][height];
            int blue[] = new int[256];
            Arrays.fill(blue,0);
            //data存rgb数据
            for(int i=0;i<width;i++){
                for(int j=0;j<height;j++){
                    data[i][j]=bimg.getRGB(i,j);
                    blue[getBlue(data[i][j])]++;
                }
            }
            //算最小阈值
            int p1 = Integer.MIN_VALUE;
            int p2 = Integer.MIN_VALUE;
            int threshold = Integer.MAX_VALUE;
            for(int i=0;i<=128;i++){
                if(p1<blue[i]) p1=i;
            }
            for(int i=129;i<=255;i++){
                if(p2<blue[i]) p2=i;
            }
            for(int i=p1;i<=p2;i++){
                if(threshold>blue[i]) threshold = i;
            }
            System.out.println(threshold);
            //阈值上面画白的下面画黑的
            for(int i=0;i<width;i++){
                for(int j=0;j<height;j++){
                    if(threshold-getBlue(data[i][j])<0){
                        data[i][j]=0xffffff;
                    }
                    else {
                        data[i][j]=0x000000;
                    }
                }
            }
            List<Point> pointList = new ArrayList<>();

            int prei=0, prej=0;
            for(int i=0;i<width;i++){
                boolean flag = true;
                for(int j=0;j<height;j++){
                    if (flag&&data[i][j]==0) {
                        if(i==0){
                            prej=j;//第一个黑点的纵坐标
                            pointList.add(new Point(prei,prej));//把第一个点丢进去先
                        }
                        flag=false;
                        if(Math.abs(j-prej)<=Integer.MAX_VALUE){//如果这个点是无效点就跳过，是有效点就丢到绘制列表里去
                            pointList.add(new Point(i,j));
                            prej=j;
                        }

                    }
                    else data[i][j]=0xffffff;
                }
            }

            System.out.println("size is : "+pointList.size());


            for (int i = 1; i < pointList.size(); i++) {
                getRoute(new Point((int)pointList.get(i-1).getX(), (int)pointList.get(i-1).getY()),
                        new Point((int)pointList.get(i).getX(), (int)pointList.get(i).getY())).forEach(e->{
                    data[(int)e.getX()][(int)e.getY()] = 0x000000;
                });
            }

            System.out.println("Start to write!");



            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for(int i=0;i<width;i++){
                for(int j=0;j<height;j++){
                    bufferedImage.setRGB(i,j,data[i][j]);
                }
            }
            ImageIO.write(bufferedImage, "jpg", new File("out.jpg"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //一个rgb值，按照RGB的顺序每两位存一个值
    public static int getBlue(int x){
        return x& 0xff;
    }

    //给两个点，返回连线的路径
    public static List<Point> getRoute(Point aa, Point bb){
        Point a = new Point(aa);
        Point b = new Point(bb);
        Double[] d = new Double[8];
        List<Point> pointList = new ArrayList<>();
        while(a.getX()!=b.getX() || a.getY()!=b.getY()){

            int x = (int)a.getX();
            int y = (int)a.getY();
            d[0] = getDistance(new Point(x-1,y-1),b);
            d[1] = getDistance(new Point(x-1,y),b);
            d[2] = getDistance(new Point(x-1,y+1),b);
            d[3] = getDistance(new Point(x,y-1),b);
            d[4] = getDistance(new Point(x,y+1),b);
            d[5] = getDistance(new Point(x+1,y-1),b);
            d[6] = getDistance(new Point(x+1,y),b);
            d[7] = getDistance(new Point(x+1,y+1),b);

            double m = Collections.min(Arrays.asList(d));
            //有可能两个点是相邻的，可以直接返回了
            if(m==0) return pointList;
            int k = 0;
            for (int i = 0; i < d.length; i++) {
                if(d[i]==m){
                    k=i;
                    break;
                }
            }
            switch (k){
                case 0:a.setLocation(x-1,y-1);pointList.add(new Point(a));break;
                case 1:a.setLocation(x-1,y);pointList.add(new Point(a));break;
                case 2:a.setLocation(x-1,y+1);pointList.add(new Point(a));break;
                case 3:a.setLocation(x,y-1);pointList.add(new Point(a));break;
                case 4:a.setLocation(x,y+1);pointList.add(new Point(a));break;
                case 5:a.setLocation(x+1,y-1);pointList.add(new Point(a));break;
                case 6:a.setLocation(x+1,y);pointList.add(new Point(a));break;
                case 7:a.setLocation(x+1,y+1);pointList.add(new Point(a));break;
                default:break;
            }

        }
        return pointList;

    }

    //算几何距离
    public static double getDistance(Point a, Point b){
        double x = a.getX()-b.getX();
        double y = a.getY()-b.getY();
        double ans = Math.sqrt(x*x+y*y);
        return ans;
    }

    public static void main(String [] args){
        ImgHandler imgHandler = new ImgHandler();
        imgHandler.solve("zgc.jpg");
    }
}
