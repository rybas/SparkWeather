package com.rybas.sparkWeather;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkWeather05 {

	public static final void main(final String[] parametros) {

		final SparkConf sparkConf = new SparkConf().setAppName("SparkWeather05").setMaster("local");
		final JavaSparkContext spark = new JavaSparkContext(sparkConf);
		final SparkSession sqlContext = SparkSession.builder().getOrCreate();

		Dataset<Row> datosMeteorologicos = obtenerDatos(sqlContext);
		datosMeteorologicos = datosMeteorologicos.select("Time", "Indoor_Temperature");
		datosMeteorologicos = datosMeteorologicos.filter("Indoor_Temperature < 10");

		datosMeteorologicos.write().json("hdfs://localhost:9000/user/rybas/sparkWeather/");

		spark.close();
	}

	private static Dataset<Row> obtenerDatos(final SparkSession sqlContext) {
		final String path = "/Users/osboxes/git/SparkWeatherJava/program/src/main/resources/EasyWeather.txt";
		final Dataset<Row> datosMeteorologicos = sqlContext.read().format("com.databricks.spark.csv").option("inferSchema", "true")
				.option("header", "true").option("delimiter", "\t").load(path);
		return datosMeteorologicos;
	}

}
