/* Desember 2009
** Coder : haripinter
** Open Content (x) Copyleft
** haripinter at gmail d0t com
** aanx_xreater at yahoo d0t com
*/
  
class bpx{
  //------ input
  // Jumlah data
  static int jumlah_data=4;
  // Unit Input 2
  static int unit_input=2;
  // Unit Hidden 2
  static int unit_hidden=2;
  // Unit Output 1
  static int unit_output=1;
 
  final double alfa=0.05;
  final double stopping=0.05;
 
 
  //------ hidden
  //Unit input pada Hidden (z_in)
  static double z_in[]=new double[unit_hidden];
  //Input pada Hidden (z)
  static double z[]=new double[z_in.length];
 
  //Bias pada unit Hidden (bH)
  static double v0[]=new double[unit_hidden];
  //Delta Bias pada unit Hidden (bHx)  --- untuk perbaikan bias Hidden
  static double v0x[]=new double[v0.length];
  //Bobot antara Input-HIdden (v)
  static double v[][]=new double[unit_input][unit_hidden];
  //Delta Bobot antara Input-HIdden (vx) --- untuk perbaikan  bobot Input-Hidden
  static double vx[][]=new double[v.length][v[0].length];
 
  //Kesalahan pada setelah Hidden (Err_z)
  static double Err_in[]=new double[unit_hidden];
  //Kesalahan pada Hidden (Err_z)
  static double Err_z[]=new double[unit_hidden];
 
  //------ output
  //Unit Output pada Output (y_in)
  static double y_in[]=new double[unit_output];
  //Output pada Output (y)
  static double y[]=new double[y_in.length];
 
  //Bias pada unit Output (bO)
  static double w0[]=new double[unit_hidden];
  //Delta Bias pada unit Output (bOx)  ---- untuk perbaikan bias pada Output
  static double w0x[]=new double[w0.length];
  //Bobot antara Hidden-Output <span class="wp-smiley wp-emoji wp-emoji-wordpress" title="(w)">(w)</span>
  static double w[][]=new double[unit_hidden][unit_output];
  //Delta Bobot antara Hidden-Output (wx) --- untuk perbaikan bobot Hidden-Output
  static double wx[][]=new double[w.length][w[0].length];
 
  //Kesalahan pada Ouput (Err_y)
  static double Err_y[]=new double[unit_output];
 
  //------------- aha
  //Minimum Error Kuadrat ERR
  static double ERR;
 
  //penentuan berhenti atau lanjut
  double cekStop(double x[][], double v0[],double v[][],double w0[],double w[][], double t[]){
    double akumY=0;
    //~ itung z_in dan z
    for(int h=0; h<jumlah_data; h++){
     for(int j=0; j<unit_hidden; j++){
      //itung sigma xi vij
      double jum_xv=0;
      for(int i=0; i<unit_input; i++){
         double cc=x[h][i]*v[i][j];
         jum_xv=jum_xv+cc;
         //System.out.println(x[h][j]);
      }
      z_in[j]=v0[j]+jum_xv;
      //itung z
      z[j]=1/(1+(double)Math.exp(-z_in[j]));
      //System.out.println(" dan z= "+z[j]);
     }
 
     //~ itung y_in dan y     (output)
      double cxc=0;
      for(int k=0; k<unit_output; k++){
        double jum_zw=0;
        for(int j=0; j<unit_hidden; j++){
          double cc=z[j]*w[j][k];
          jum_zw=jum_zw+cc;
        }
        y_in[k]=w0[k]+jum_zw;
        y[k]=1/(1+(double)Math.exp(-y_in[k]));
        akumY = akumY + Math.pow((t[h]-y[k]),2);
        //System.out.println(t[h]+"-"+y[k]+"="+(t[k]-y[k]));
      }
    }
    double E = 0.5 * akumY;
    //System.out.println(E);
    return E;
  }
 
  void learn(double x[][],double v0[], double v[][],double w0[],double w[][],double t[]){
    do{
      //~ itung z_in dan z
      for(int h=0; h<jumlah_data; h++){
        for(int j=0; j<unit_hidden; j++){
          //itung sigma xi vij
          double jum_xv=0;
          for(int i=0; i<unit_input; i++){
            double cc=x[h][i]*v[i][j];
            jum_xv=jum_xv+cc;
          }
          z_in[j]=v0[j]+jum_xv;
          //itung z
          z[j]=1/(1+(double)Math.exp(-z_in[j]));
        }
 
        //~ itung y_in dan y     (output)
        double cxc=0;
        for(int k=0; k<unit_output; k++){
          double jum_zw=0;
          for(int j=0; j<unit_hidden; j++){
            double cc=z[j]*w[j][k];
            jum_zw=jum_zw+cc;
          }
          y_in[k]=w0[k]+jum_zw;
          y[k]=1/(1+(double)Math.exp(-y_in[k]));
          //System.out.println(y[k]);
        }
        //System.out.println(y[0]);
 
        //ngitung error output dan delta bias dan delta bobot
        for(int k=0; k<unit_output; k++){
          //error otput
          Err_y[k]=(t[h]-y[k])*y[k]*(1-y[k]);
          //System.out.println(Err_y[k]);
 
          double  cc=0;
          for(int j=0; j<unit_hidden; j++){
            //delta bobot hO
            wx[j][k]=alfa*Err_y[k]*z[j];
            //delta bias hO
            w0x[k]=alfa*Err_y[k];
            //System.out.println("delta wx = "+wx[j][k]);
          }
          //System.out.println("delta w0 = "+w0x[k]);
        }
 
        //ngitung error hiden dan delta bias dan delta bobot
        for(int j=0; j<unit_hidden; j++){
          double cc=0;
          for(int k=0; k<unit_output; k++){
            cc = cc + (Err_y[k]*w[j][k]);
          }
          // eror sebelum output / setelah hidden
          Err_in[j]=cc;
          //System.out.println(Err_in[j]);
 
          // eror hidden               (t[h]-y[k])*y[k]*(1-y[k]);
          Err_z[j]=Err_in[j]*(z[j])*(1-z[j]);
          //System.out.println(Err_z[j]);
 
          for(int i=0; i<unit_input; i++){
            //delta bobot iH
            vx[i][j]=alfa*Err_z[j]*x[h][i];
            //System.out.println("delta vx = "+vx[i][j]);
          }
          //delta bias  hidden
          v0x[j]=alfa*Err_z[j];
          //System.out.println("delta v0 = "+v0x[j]);
          //System.out.println(alfa+" "+Err_z[j]+" "+v0x[j]);
        }
 
        //update bobot dan bias
        //update bobot bias outpuut
        for(int j=0; j<unit_hidden; j++){
          for(int k=0; k<unit_output; k++){
            w[j][k]=w[j][k]+wx[j][k];
            //w0[k]=w0[k]+w0x[k];
            //System.out.println("w = "+w[j][k]);
          }
        }
        for(int k=0; k<unit_output; k++){
          //w[j][k]=w[j][k]+wx[j][k];
          w0[k]=w0[k]+w0x[k];
          //System.out.println("w0 = "+w0[k]);
        }
 
        //update bobot bias hidden
        for(int i=0; i<unit_input; i++){
          for(int j=0; j<unit_hidden; j++){
            v[i][j]=v[i][j]+vx[i][j];
            //v0[j]=v0[j]+v0x[j];
            //System.out.println("v = "+v[i][j]);
          }
        }
 
        for(int j=0; j<unit_hidden; j++){
          //v[i][j]=v[i][j]+vx[i][j];
          v0[j]=v0[j]+v0x[j];
          //System.out.println("v0 = "+v0[j]);
        }
      }
      //System.out.println(cekStop(x,v0,v,w0,w,t));
    }while(cekStop(x,v0,v,w0,w,t)>stopping);
 
 
    /// bagian ini untuk ngeprint doang...
    /// jadi dihapus gpp
 
    for(int j=0; j<unit_hidden; j++){
      for(int k=0; k<unit_output; k++){
        System.out.println("w = "+w[j][k]);
      }
    }
    for(int k=0; k<unit_output; k++){
      System.out.println("w0 = "+w0[k]);
    }
    for(int i=0; i<unit_input; i++){
      for(int j=0; j<unit_hidden; j++){
        System.out.println("v = "+v[i][j]);
      }
    }
    for(int j=0; j<unit_hidden; j++){
      System.out.println("v0 = "+v0[j]);
    }
  }
 
  public static void test(double inputan[], double v0[],double v[][],double w0[],double w[][], double t[])
  {
    //pada hidden
    for(int j=0; j<unit_hidden; j++)
    {
      double cc=0;
      for(int i=0; i<inputan.length; i++){
        cc= cc + (inputan[i]*v[i][j]);
      }
      z_in[j] = v0[j] +cc;
      z[j] = 1/(1+(double)Math.exp(-z_in[j]));
    }
 
    //pada ouotpr
    for(int k=0; k<unit_output; k++){
      double cc = 0;
      for(int j=0; j<unit_hidden; j++){
        cc = cc + z[j]*w[j][k];
      }
      y_in[k] = w0[k]+cc;
 
      double y = 1/(1+(double)Math.exp(-y_in[k]));
 
      if(y>0.5)
       y=1;
      else
       y=0;
      System.out.println("Output "+(double)y);
    }
 
 
  }
 
  public static void main(String haripinter[]){
 
    System.out.println("------------------------");
    System.out.println("Do you know...? that... ");
    System.out.println("i never give up!");
    System.out.println("Let's grow with me.... ");
    System.out.println("------------------------");
 
    bpx pb = new bpx();
 
    //inisialisasi data mentah
    double x[][]={{0,0},{0,1},{1,0},{1,1}};
    //inisialisasi target
    double t[]={0,1,1,0};
    //inisialisasi bias pada unit hidden
    double v0[]={0.318946,0.12234};
    //inisialisasi bobot pada input-hidden
    double v[][]={{0.263178,0.43222},{0.43092,0.22224}};
    //inisialisasi bias pada unit output
    double w0[]={0.41180};
    //inisialisasi bobot pada hidden-output
    double w[][]={{0.33960},{0.43333}};
 
    //proses learning
    pb.learn(x,v0,v,w0,w,t);
 
    double r[] = {0,0};
    double s[] = {0,1};
    double u[] = {1,0};
    double m[] = {1,1};
 
    pb.test(r,v0,v,w0,w,t);
    pb.test(s,v0,v,w0,w,t);
    pb.test(u,v0,v,w0,w,t);
    pb.test(m,v0,v,w0,w,t);
 
  }
}