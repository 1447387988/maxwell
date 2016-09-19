package com.zendesk.maxwell;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zendesk.maxwell.producer.MaxwellOutputConfig;
import com.zendesk.maxwell.schema.ddl.ResolvedSchemaChange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class DDLMap extends RowMap {
	private final ResolvedSchemaChange change;
	private final Long timestamp;
	private final String sql;

	public DDLMap(ResolvedSchemaChange change, Long timestamp, String sql, BinlogPosition nextPosition) {
		super("ddl", "database", "table", timestamp, new ArrayList<String>(0), nextPosition);
		this.change = change;
		this.timestamp = timestamp;
		this.sql = sql;
	}

	public String pkToJson(KeyFormat keyFormat) throws IOException {
		return UUID.randomUUID().toString();
	}

	public boolean isTXCommit() {
		return false;
	}

	public String toJSON() throws IOException {
		return toJSON(new MaxwellOutputConfig());
	}

	public String toJSON(MaxwellOutputConfig outputConfig) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		//return mapper.writeValueAsString(change);

		Map<String, Object> changeMixin = mapper.convertValue(change, new TypeReference<Map<String, Object>>() { });
		changeMixin.put("ts", timestamp);
		changeMixin.put("sql", sql);
		return mapper.writeValueAsString(changeMixin);
	}
}

