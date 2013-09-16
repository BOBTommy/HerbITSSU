package Approximator;

public class Nth_InterpolatingPolynomial {
	private int x[];
	private long y[];
	private long blocked_func[][];
	private long n_th_poly[];
	private int targetX;
	private int n;				//y의 배열크기를 대입
	private int target_n;		//n차 함수까지 계산
	/**
	 * 뉴턴의 보간다항식
	 * @param x : x축의 값들을 가지는 int[]
	 * @param y : y축의 값들을 가지는 int[], y축 갯수와 x축 갯수는 같아야함
	 * @param targetX : x축에서 값을 구하고자 하는 x좌표값
	 * @param n : 구하고자 하는 추정함수의 차수 , 1부터  x,y배열 원소갯수-1 까지
	 */
	public Nth_InterpolatingPolynomial(int x[], long y[], int targetX, int n_t)
	{
		this.x = x;
		this.y = y;
		this.n = y.length -1;
		this.target_n = n_t;
		blocked_func = new long[n+2][n+2];
		n_th_poly = new long [n+2];
		this.targetX = targetX;
	}
	public long getVal() {
		int result = -1;
		if (x.length <2) {
			return y[0];
		}
		if (x != null) {
			/* approximation start */
			
			int i=0;
			do
			{
				blocked_func[i][i] = y[i];
			} while (++i <=n);
			int j=1;
			do
			{
				int k=0;
				do
				{
					
					blocked_func[ k ][ k+j ]
							= (blocked_func[ k+1 ][ k+j ] - blocked_func[ k ][ k+j-1 ]) / (x[ k+j ] - x[ k ]);
				} while(++k <= n-j);
			} while (++j <= n-1);
			
			n_th_poly[0] = y[0];
			
			int m=1;
			do
			{
				int var = 1;
				for (int tmp=0; tmp<m; tmp++) {
					var *= targetX - x[ tmp ];
				}
				
				n_th_poly[ m ] = n_th_poly[ m-1 ] + var * blocked_func[ 0 ][ m ];
				
			} while (++m <= target_n);
			/* approximation end */

			
			return n_th_poly[ target_n ];
		}
		return result;
	}
	
}
