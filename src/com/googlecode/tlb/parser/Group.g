grammar Group;


@lexer::header {
package com.googlecode.tlb.parser;

} 

@header{
package com.googlecode.tlb.parser;
	
import com.googlecode.tlb.Groups;
import com.googlecode.tlb.Group;

}

@members {
Groups groups = new Groups();
Group group = new Group();

}


groups	returns[Groups value = new Groups()] :	(group)+{return this.groups;};
group	:	'[' jobs ']'{this.group = new Group();};
jobs 	:	job+{this.groups.add(group);};
job	:	JOB{this.group.addJob($JOB.text);} | ',' JOB{this.group.addJob($JOB.text);};


JOB 	:	 ('a'..'z' | 'A'..'Z' | '0' .. '9' | '_' | '-')+;
WS 	: 	(' '|'\t'|'\n'|'\r')+ {skip();} ; 
