package neuralNetwork;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Convolution {
	public List<double[][]> filters = Arrays.asList(//
			new double[][] { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, 1 } }, // |
			new double[][] { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } }, // ！！
			new double[][] { { 0, -1, 1 }, { 0, -1, 1 }, { 0, -1, 1 } }, // |
			new double[][] { { 0, 1, -1 }, { 0, 1, -1 }, { 0, 1, -1 } }, // |
			new double[][] { { 1, 0, -1 }, { 1, 0, -1 }, { 1, 0, -1 } }, // |
			// new double[][] { { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } }, // |
			new double[][] { { 1, -1, 0 }, { 1, -1, 0 }, { 1, -1, 0 } }, // |
			new double[][] { { -1, 1, 0 }, { -1, 1, 0 }, { -1, 1, 0 } }, // |
			new double[][] { { 0, 0, 0 }, { 1, 1, 1 }, { -1, -1, -1 } }, // ！！
			new double[][] { { 0, 0, 0 }, { -1, -1, -1 }, { 1, 1, 1 } }, // ！！
			// new double[][] { { 1, 1, 1 }, { 0, 0, 0 }, { -1, -1, -1 } }, // ！！
			new double[][] { { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } }, // ！！
			new double[][] { { 1, 1, 1 }, { -1, -1, -1 }, { 0, 0, 0 } }, // ！！
			new double[][] { { -1, -1, -1 }, { 1, 1, 1 }, { 0, 0, 0 } }, // ！！
			new double[][] { { 1, 1, 0 }, { 1, 0, -1 }, { 0, -1, -1 } }, ///
			new double[][] { { 0, 1, 1 }, { -1, 0, 1 }, { -1, -1, 0 } }, // \
			new double[][] { { 0, -1, -1 }, { 1, 0, -1 }, { 1, 1, 0 } }, // \
			new double[][] { { -1, -1, 0 }, { -1, 0, 1 }, { 0, 1, 1 } }); ///

	public double[][] firstLayerFeatureMapsSum;
	public List<double[][]> firstLayerFeaturesVector, secondLayerFeaturesVector;

	public void setupAllLayers(double[][] inputs) {
		firstLayerFeaturesVector = features(inputs, filters, 1, true, true, 2, 2);
		firstLayerFeatureMapsSum = matrixAddition(firstLayerFeaturesVector);
		secondLayerFeaturesVector = features(firstLayerFeatureMapsSum, filters, 1, true, true, 2, 2);
	}

	public List<Double> getFeatureVector() {
		return secondLayerFeaturesVector.stream().map(f -> Arrays.stream(f).map(Arrays::stream)//
				.map(e -> e.boxed()).flatMap(s -> s)).flatMap(s -> s).collect(Collectors.toList());
	}

	public double[][] matrixAddition(List<double[][]> features) {
		double[][] outputs = new double[features.get(0).length][features.get(0).length];
		for (double[][] feature : features)
			for (int y = 0; y < features.get(0).length; y++)
				for (int x = 0; x < features.get(0).length; x++)
					outputs[y][x] += feature[y][x] / 100;
		return outputs;
	}

	public List<double[][]> features(double[][] inputs, List<double[][]> filters, int stride, boolean padding,
			boolean relu, int poolingFilterSize, int poolingStride) {
		return filters.stream().map(f -> pooling(convolution(inputs, f, stride, padding, relu)//
				, poolingFilterSize, poolingStride)).collect(Collectors.toList());
	}

	public double[][] createDrawing(double[][] inputs) {
		return matrixAddition(filters.stream().map(f -> convolution(inputs, f, 1, true, true)//
		).collect(Collectors.toList()));
	}

	public double[][] pooling(double[][] inputs, int filterSize, int stride) {
		int size = (int) Math.floor(inputs.length / stride);
		double[][] output = new double[size][size];
		for (int y = 0; y < output.length; y++)
			for (int x = 0; x < output[0].length; x++)
				output[y][x] = max(inputs, filterSize, x * stride, y * stride);
		return output;
	}

	public double max(double[][] inputs, int filterSize, int X, int Y) {
		double output = 0;
		for (int y = 0; y < filterSize; y++)
			for (int x = 0; x < filterSize; x++)
				output = output > inputs[Y + y][X + x] ? output : inputs[Y + y][X + x];
		return output;
	}

	public double[][] convolution(double[][] inputs, double[][] filter, int stride, boolean padding, boolean relu) {
		inputs = padding ? pad(inputs, (filter.length - 1) / 2) : inputs;
		int size = (int) Math.ceil((inputs.length - filter.length + 1) / stride);
		double[][] output = new double[size][size];
		for (int y = 0; y < output.length; y++)
			for (int x = 0; x < output[0].length; x++)
				output[y][x] = relu ? Math.max(0, convolveOnePixel(inputs, filter, x * stride, y * stride))
						: convolveOnePixel(inputs, filter, x * stride, y * stride);
		return output;
	}

	public double convolveOnePixel(double[][] inputs, double[][] filter, int X, int Y) {
		double output = 0;
		for (int y = 0; y < filter.length; y++)
			for (int x = 0; x < filter[0].length; x++)
				output += filter[y][x] * inputs[Y + y][X + x];
		return output;

	}

	public double[][] pad(double[][] inputs, int size) {
		double[][] output = new double[inputs.length + size * 2][inputs[0].length + size * 2];
		for (int y = 0; y < inputs.length; y++)
			System.arraycopy(inputs[y], 0, output[y + size], size, inputs[y].length);
		return output;
	}
}
