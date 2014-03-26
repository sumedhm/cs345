import java.util.Scanner;

class Complex{
	public double real = 0;
	public double img = 0;
	
	public Complex(double real, double img){
		this.real = real;
		this.img = img;
	}
	
	public Complex add(Complex num){
		return new Complex(this.real + num.real, this.img + num.img);
	}
	
	public Complex subtract(Complex num){
		return new Complex(this.real - num.real, this.img - num.img);
	}
	
	public Complex multiply(Complex num){
		return new Complex((this.real*num.real)-(this.img*num.img), (this.real*num.img)+(this.img*num.real));
	}
	
	public double getReal(){
		return this.real;
	}
	
	public double getImg(){
		return this.img;
	}
	
	public Complex inverse(){
		double abs = this.getVal();
		abs *= abs;
		return new Complex((this.real/abs), -(this.img/abs));
	}
	
	public double getVal(){
		return Math.sqrt((this.real*this.real)+(this.img*this.img));
	}
}


public class Main {
	
	public static int nearestExpOfTwo(int n){
		double i=0; int ans=0;
	        while(ans < n){
	                ans = (int) Math.pow(2, i);
	                i++;
	        }
	        return ans;
	}
	
	public static void FFT(Complex[] arr, int size, Complex w, Complex[] ans){
		if (size==1){
			ans[0] = arr[0];
		} else {
			
			Complex[] A_odd = new Complex[size/2];
			Complex[] F_odd = new Complex[size/2];
			Complex[] A_even = new Complex[size/2];
			Complex[] F_even = new Complex[size/2];
			int j = 0;
			for(int i=0;i<size;i=i+2){
				A_even[j] = arr[i];
				F_even[j] = new Complex(0, 0);
				j++;
			}
			j = 0;
			
			for(int i=1;i<size;i=i+2){
				A_odd[j] = arr[i];
				F_odd[j] = new Complex(0, 0);
				j++;
			}

			FFT(A_even, size/2, w.multiply(w), F_even);    //w^2 is a primitive n/2-th root of unity
			FFT(A_odd, size/2, w.multiply(w), F_odd);

			Complex x = new Complex(1, 0);
			
			for (j=0; j < size/2; ++j){
				ans[j] = F_even[j].add(x.multiply(F_odd[j]));
				ans[j+size/2] = F_even[j].subtract(x.multiply(F_odd[j]));
				x = x.multiply(w);
			}
		}
	}
	
	public static void main(String[] args) {
		int testcases, n;
		Scanner s = new Scanner(System.in);
		
		testcases = s.nextInt();
		while(testcases>0){
			n = s.nextInt();
			n++;
			int degree = n;
			Complex[] A = new Complex[4*n];
			Complex[] B = new Complex[4*n];
			
			for(int i=0;i<n;i++){
				A[i] = new Complex(s.nextInt(), 0);
			}
			Complex[] C = new Complex[4*n];

			for(int i=0;i<n;i++){
				B[i] = new Complex(s.nextInt(), 0);
				C[i] = new Complex(0, 0);
			}
						
			Complex[] Apv = new Complex[4*n]; 	//Point value representation of A on 2n points.
			Complex[] Bpv = new Complex[4*n]; 	//Point value representation of B on 2n points.
			Complex[] Cpv = new Complex[4*n]; 	//Point value representation of B on 2n points.

			for(int i=n;i<4*n;i++){
				A[i] = B[i] = C[i] = Apv[i] = Bpv[i] = Cpv[i] = new Complex(0, 0);
			}

			n = nearestExpOfTwo(n);
			double x = Math.cos(Math.PI/n);
            double y = Math.sin(Math.PI/n);
            Complex w = new Complex(x, y);
			FFT(A, 2*n, w, Apv);
			FFT(B, 2*n, w, Bpv);
			
			for(int i=0;i<2*n;i++){
				Cpv[i] = Apv[i].multiply(Bpv[i]);
			}
						
			FFT(Cpv, 2*n, w.inverse(), C);
			
			for(int i=0;i<2*degree-1;i++){
				long term = Math.round(C[i].getReal()/(2*n));
				System.out.print(term+" ");
			}
			
			testcases--;
		}
		s.close();
		return;
	}

}
