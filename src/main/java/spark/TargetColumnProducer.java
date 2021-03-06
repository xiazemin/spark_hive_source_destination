/*
 * Copyright (c) 2016 Villu Ruusmann
 *
 * This file is part of JPMML-Spark
 *
 * JPMML-Spark is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JPMML-Spark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with JPMML-Spark.  If not, see <http://www.gnu.org/licenses/>.
 */
package spark;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.dmg.pmml.DataField;
import org.dmg.pmml.DataType;
import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.Evaluator;

class TargetColumnProducer extends ColumnProducer {

	TargetColumnProducer(FieldName fieldName, String columnName){
		super(fieldName, columnName != null ? columnName : (fieldName != null ? fieldName.getValue() : "_target"));
	}

	@Override
	public StructField init(Evaluator evaluator){
		FieldName fieldName = getFieldName();

		DataField dataField = evaluator.getDataField(fieldName);
		if(dataField == null){
			throw new IllegalArgumentException();
		}

		DataType dataType = dataField.getDataType();

		String columnName = getColumnName();

		return DataTypes.createStructField(columnName, SchemaUtil.translateDataType(dataType), false);
	}

	@Override
	public Object format(Object value){
		return org.jpmml.evaluator.EvaluatorUtil.decode(value);
	}
}