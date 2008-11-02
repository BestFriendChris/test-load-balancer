<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xslt="http://xml.apache.org/xslt" xmlns:stringutils="xalan://org.apache.tools.ant.util.StringUtils" version="1.0">
	<xsl:output method="html" indent="yes" encoding="US-ASCII" standalone="yes"/>
<xsl:decimal-format decimal-separator="." grouping-separator="," />
  <xsl:param name="indent-increment" select="'   '" />
<xsl:template match="testsuites">
    <html>
        <head>
            <title>Twist Test Results</title>
    <style type="text/css" media="screen">
      body {
        font:normal 68% verdana,arial,helvetica;
        color:#000000;
		padding: 0;
		margin: 0;
      }
	  ul {
	  	margin: 5px 0 0 25px;
		padding: 0;
	  }
	  #logo {
	  	height: 80px;
	  	width: 100%;
		background: url(junit-images/header.jpg) repeat-x;
	  }
      table tr td, table tr th {
          font-size: 68%;
      }
	  table.details{
	  	width: 100%;
	  	border-right: 1px solid #E0DFD5;
	  }
      table.details tr th{
        font-weight: bold;
        text-align:left;
        background:#F1F0EC;
		color:#777;
		font-size: 12px;
		border-bottom: 1px solid #E0DFD5;
		border-left: 1px solid #E0DFD5;
      }
      table.details tr td{
        background:#fff;
		border-left:1px solid #E0DFD5;
		border-bottom:1px solid #E0DFD5;
      }
	  table.details tr td a:link, a:visited {
	  	color: #666;
	  }

	  table.details tr td a:hover {
	  	color: #AEAEAE;
	  }

      p {
        margin: 0;
		padding: 0;
      }
      div#header {
		background: url(junit-images/header_logo.jpg) no-repeat 0 0;
		height: 80px;
		margin: 0 0 30px 0;
		position: relative;
      }
	  div#header h1 {
        margin: 0;
		padding: 3px 0 0 0;
		font-family: verdana,arial,helvetica;
		font-size: 12px;
		letter-spacing: 1px;
		color: #BACB8D;
		position: absolute;
		top: 49px;
		left: 15px;
		display: block;
		width: 95%;
		border-top: 1px solid #8BA87A;
		background: transparent;
	  }
	  div#header #watermark {
	  	float: right;
		width: 118px;
		height: 80px;
        margin: 0;
		padding: 0;
		position: relative;
		background: url(junit-images/watermark.jpg) no-repeat 0 0;
	  }
	  #page_wrapper {
	  	padding: 0 25px 0 25px;
		margin: 0;
	  }
      h2 {
        margin: 0;
		font-size: 14px;
      }
      h3 {
						margin-bottom: 0.5em;
						font: bold 115% verdana,arial,helvetica
      }
      h4 {
						margin-bottom: 0.5em;
						font: bold 100% verdana,arial,helvetica
      }
      h5 {
						margin-bottom: 0.5em;
						font: bold 100% verdana,arial,helvetica
      }
      h6 {
						margin-bottom: 0.5em;
						font: bold 100% verdana,arial,helvetica
      }
      .Error {
						font-weight: bold;
						color: #F06652;
      }
      .Failure {
        font-weight:bold;
		color: purple;
      }
	  .summary-text {
        font-weight:bold;
		color: #669;
		font-size: 22px;
	  }
	  .table-header {
		background: url(junit-images/table_header.png) no-repeat top left #E0DFD5;
		border-bottom: 1px solid #A3A3A3;
		padding: 0 0 5px 0;
	  }
	  .table-header h2 {
	  	padding: 10px 20px 0 20px;
		margin: 0;
		color: #666;
	  }
	  .clear {
	  	clear: both;
	  }
	  .back-to-top {
	  	margin: 10px 0 20px 0;
	  }
      div.scenario {
        color: black;
        margin: 0 30px 20px 30px;
        padding: 10px;
		background: #FDFEF8;
		border: 1px solid #D7E1D1;
		font-family: Arial, Helvetica, sans-serif;
		font-weight: lighter;
      }

	  div.scenario h1 {
	  	font-size: 20px;
		margin: 20px 0 7px 0;
		padding: 0;
	  }

      div.scenario p{
	  	margin: 0 0 5px 0;
        font-size: 12px;
      }

      div.scenario td {
        background: white;
      }

      div.scenario .thumbnail {
        border-width: 1px;
        border-style: solid;
        border-color: black;
      }
      .error-line {
		border: 1px solid #AA6270;
		background: #F06652;
		color: white;
		font-weight: bolder;
		font-size: 12px;
		padding: 0 0 0 5px;
      }
	  div#summary-module {
	  	width: 488px;
		height: 149px;
		padding: 0;
		margin: 0 0 20px 0;
		background: url(junit-images/summary.jpg) no-repeat 0 0;
	  }
	  div#summary-module h2 {
	  	color: #666666;
		font-size: 18px;
		margin: 10px 20px 5px 20px;
		padding: 10px 0 0 0;
	  }
	  div#summary-module table {
		margin: 0 0 0 20px;
		width: 445px;
	  }
	  div#summary-module p {
		padding: 0 20px 0 20px;
	  }
	  div#packages-module {
	  	width: 890px;
	  	margin: 0 0 30px 0;
	  }
	  div#packages-module p {
	  	margin: 0;
		padding: 0 20px 0 20px;
	  }
	  div#scenarios-module {
	  	width: 890px;
	  	margin: 0 0 30px 0;
	  }
	  div#scenarios-module h2 {
	  	text-transform: capitalize;
	  }
	  div#scenarios-module p {
	  	margin: 0;
		padding: 0 20px 0 20px;
	  }
	  div#testcase-module {
	  	width: 890px;
	  	margin: 0 0 30px 0;
	  }
	  .scenario-table {
		width: 90%;
		border-top: 0px solid #ccc;
		border-right: 0px solid #ccc;
		padding: 0;
		margin: 0;
	  }
	  table.scenario-table tbody {
	  	margin: 0;
		padding: 0;
		border: 0;
		background: green;
	  }

					div.expandable {
						display: none;
					}
      </style>

	  <style media="print">
	  	body {
			background: transparent;
			font-size: 10px;
			font-family: Arial, Helvetica, sans-serif;
		}
		a {
			text-decoration: none;
			color: black;
		}
	  	ul {
	  		margin: 10px 0 0 20px;
			padding: 0;
	  	}
		#logo {
	  		height: 80px;
	  		width: 100%;
			background: url(junit-images/header.jpg) repeat-x;
	  	}
		div#header {
			background: url(junit-images/header_logo.jpg) no-repeat 0 0;
			height: 80px;
			margin: 0 0 30px 0;
			position: relative;
      	}
		div#header h1 {
        	margin: 0;
			padding: 3px 0 0 0;
			font-family: verdana,arial,helvetica;
			font-size: 12px;
			letter-spacing: 1px;
			color: #BACB8D;
			position: absolute;
			top: 49px;
			left: 15px;
			display: block;
			width: 95%;
			border-top: 1px solid #8BA87A;
	  	}
	  	div#header #watermark {
	  		float: right;
			width: 118px;
			height: 80px;
        	margin: 0;
			padding: 0;
			position: relative;
			background: url(junit-images/watermark.jpg) no-repeat 0 0;
	  	}
		#page_wrapper {
	  		padding: 0;
			margin: 0;
		}
		h1 {
			font-size: 14px;
		}
		h2 {
			font-size: 12px;
			margin: 0;
		}
		h3 {
			font-size: 10px;
		}
		p {
			font-size: 10px;
			margin: 0;
			padding: 0;
		}
		table {
			width: 100%;
			border-right: 1px solid #666666;
			border-bottom: 1px solid #666666;
		}
		table th {
			border-top: 1px solid #666666;
			border-left: 1px solid #666666;
			font-size: 10px;
			font-weight: bolder;
			background: #E0DFD5;
		}
		table td {
			border-top: 1px solid #666666;
			border-left: 1px solid #666666;
			font-size: 10px;
		}
		.table-header h2 {
			margin: 6px 0 0 0;
		}
		.error-line {
			border: 1px solid #FFD8B0;
			background: #FFEAD5;
		}
		div.scenario {
			padding: 0;
			margin: 0;
		}
		.back-to-top {
			display: none;
		}
		.Error {
			color: red;
			font-weight: bolder;
		}
		.Failure {
			color: purple;
			font-weight: bolder;
		}
		div#summary-module h2 {
			margin: 10px 0 0 0;
		}
	  	div#scenarios-module h2 {
	  		text-transform: capitalize;
	  	}
		div.scenario {
        	color: black;
        	margin: 0 5px 2px 5px;
        	padding: 10px;
			background: #FDFEF8;
			border: 1px solid #D7E1D1;
		}
		.scenario-table {
			width: 90%;
			border-top: 1px solid #ccc;
			border-right: 1px solid #ccc;
		}
	  </style>
				<script language="javascript">
					function collapse(id){
						theDiv = document.getElementById(id);
						if (theDiv.style.display == "block")
							theDiv.style.display = "none";
						else
							theDiv.style.display = "block";
						return false;
					}
				</script>
        </head>
        <body>
		<div id="logo">
					<a name="top"/>
            <xsl:call-template name="pageHeader"/>
		</div>
				<div class="clear"/>
		<div id="page_wrapper">

            <!-- Summary part -->
            <xsl:call-template name="summary"/>

            <!-- For each package create its part -->
            <xsl:call-template name="packages"/>

            <!-- For each class create the  part -->
            <xsl:call-template name="classes"/>
		</div>
        </body>
    </html>
</xsl:template>


    <!-- ================================================================== -->
    <!-- Write a package level report                                       -->
    <!-- It creates a table with values from the document:                  -->
    <!-- Name | Tests | Errors | Failures | Time                            -->
    <!-- ================================================================== -->
    <xsl:template name="packages">
	<div id="scenarios-module">
			<div class="table-header">
				<a name="{@package}"/>
				<h2>
		 			All Scenarios
				</h2>
			</div>

                <table class="details" border="0" cellpadding="5" cellspacing="0">
                    <xsl:call-template name="testsuite.test.header"/>
				<xsl:for-each select="/testsuites/testsuite">
					<xsl:sort select="@package"/>
                    <!-- match the testsuites of this package -->
                    <xsl:apply-templates select="/testsuites/testsuite[./@package = current()/@package]" mode="print.test"/>
				</xsl:for-each>
                </table>
				<div class="back-to-top">
                	<a href="#top">Back to top</a>
				</div>
	</div>
    </xsl:template>

    <xsl:template name="classes">
	<div id="testcase-module">
        <xsl:for-each select="testsuite[count(./testcase/failure) &gt; 0 or count(./testcase/error) &gt; 0]">
            <xsl:sort select="testcase/@name"/>
            <!-- create an anchor to this class name -->
			<div class="table-header">
           		<a name="{testcase/@name}"></a>
            	<h2><xsl:value-of select="testcase/@name"/></h2>
			</div>

            <table class="details" border="0" cellpadding="5" cellspacing="0">
              <!--
              test can even not be started at all (failure to load the class)
              so report the error directly
              -->
                <xsl:if test="./error">
                    <tr class="Error">
                        <td colspan="4"><xsl:apply-templates select="./error"/></td>
                    </tr>
                </xsl:if>
                <xsl:apply-templates select="./testcase" mode="print.test"/>
            </table>
			<div class="clear"></div>

			<div class="back-to-top">
                <a href="#top">Back to top</a>
			</div>
        </xsl:for-each>
	</div>
    </xsl:template>

    <xsl:template name="summary">
	<div id="summary-module">
        <h2>Summary</h2>
        <xsl:variable name="testCount" select="sum(testsuite/@tests)"/>
        <xsl:variable name="errorCount" select="count(testsuite[@errors &gt; 0])"/>
        <xsl:variable name="failureCount" select="count(testsuite[@failures &gt; 0])"/>
        <xsl:variable name="timeCount" select="sum(testsuite/@time)"/>
        <xsl:variable name="successRate" select="($testCount - $failureCount - $errorCount) div $testCount"/>
        <table class="details" border="0" cellpadding="5" cellspacing="0">
        <tr valign="top">
            <th>Tests</th>
            <th>Errors</th>
            <th>Failures</th>
            <th>Success rate</th>
            <th>Time</th>
        </tr>
        <tr valign="top">
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="$failureCount &gt; 0">Failure</xsl:when>
                    <xsl:when test="$errorCount &gt; 0">Error</xsl:when>
                </xsl:choose>
            </xsl:attribute>
            <td>
						<xsl:value-of select="$testCount"/>
					</td>
					<td>
						<xsl:value-of select="$errorCount"/>
					</td>
					<td>
						<xsl:value-of select="$failureCount"/>
					</td>
					<td>
                <xsl:call-template name="display-percent">
                    <xsl:with-param name="value" select="$successRate"/>
                </xsl:call-template>
            </td>
            <td>
                <xsl:call-template name="display-time">
                    <xsl:with-param name="value" select="$timeCount"/>
                </xsl:call-template>
            </td>

        </tr>
        </table>

        <p>Note: <i>failures</i> are anticipated and checked for with assertions while <i>errors</i> are unanticipated.</p>
	</div>
		<div class="clear"/>
    </xsl:template>

<!-- Page HEADER -->
<xsl:template name="pageHeader">
<div id="header">
    <h1>Twist Test Results</h1>
			<div id="watermark"/>
<!--    <table width="100%">
    <tr>
        <td align="left"></td>
        <td align="right">Designed for use with <a href='http://www.junit.org'>JUnit</a> and <a href='http://ant.apache.org/ant'>Ant</a>.</td>
    </tr>
    </table> -->
    <!--<hr size="1"/> -->
</div>
</xsl:template>

<xsl:template match="testsuite" mode="header">
    <tr valign="top">
        <th>Name</th>
        <th>Tests</th>
        <th>Errors</th>
        <th>Failures</th>
        <th nowrap="nowrap">Time(s)</th>
    </tr>
</xsl:template>

<!-- class header -->
<xsl:template name="testsuite.test.header">
    <tr valign="top">
			<th>Scenario</th>
			<th>Total Steps</th>
        <th>Errors</th>
        <th>Failures</th>
        <th nowrap="nowrap">Time(s)</th>
        <th nowrap="nowrap">Time Stamp</th>
    </tr>
</xsl:template>

<!-- class information -->
<xsl:template match="testsuite" mode="print.test">
    <tr valign="top">
        <!-- set a nice color depending if there is an error/failure -->
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="@failures[.&gt; 0]">Failure</xsl:when>
                <xsl:when test="@errors[.&gt; 0]">Error</xsl:when>
            </xsl:choose>
        </xsl:attribute>

        <!-- print testsuite information -->
        <td><a href="#{testcase/@name}"><xsl:value-of select="testcase/@name"/></a></td>
        <td><xsl:value-of select="@tests"/></td>
        <td><xsl:value-of select="@errors"/></td>
        <td><xsl:value-of select="@failures"/></td>
        <td>
            <xsl:call-template name="display-time">
                <xsl:with-param name="value" select="@time"/>
            </xsl:call-template>
        </td>
			<td>
				<xsl:apply-templates select="@timestamp"/>
			</td>
    </tr>
</xsl:template>

<xsl:template match="testcase" mode="print.test">
    <tr valign="top">
        <xsl:attribute name="class">
            <xsl:choose>
                <xsl:when test="failure | error">Error</xsl:when>
            </xsl:choose>
        </xsl:attribute>
        <xsl:choose>
            <xsl:when test="failure">
                <td><xsl:apply-templates select="failure"/></td>
            </xsl:when>
            <xsl:when test="error">
                <td><xsl:apply-templates select="error"/></td>
            </xsl:when>
            <xsl:otherwise>
                <td>Success</td>
            </xsl:otherwise>
        </xsl:choose>
    </tr>
</xsl:template>


<xsl:template match="failure">
    <xsl:call-template name="display-failures"/>
</xsl:template>

<xsl:template match="error">
    <xsl:call-template name="display-failures"/>
</xsl:template>

<!-- Style for the error and failure in the tescase template -->
<xsl:template name="display-failures">
    <xsl:choose>
        <xsl:when test="not(@message)">N/A</xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="@message"/>
        </xsl:otherwise>
    </xsl:choose>
    <!-- display the stacktrace -->
    <code>
        <br/><br/>
        <xsl:call-template name="br-replace">
            <xsl:with-param name="word" select="."/>
        </xsl:call-template>
    </code>
    <!-- the later is better but might be problematic for non-21" monitors... -->
    <!--pre><xsl:value-of select="."/></pre-->
</xsl:template>

<!--
    template that will convert a carriage return into a br tag
    @param word the text from which to convert CR to BR tag
-->
<xsl:template name="br-replace">
    <xsl:param name="word"/>
    <xsl:value-of disable-output-escaping="yes" select='stringutils:replace(string($word),"&#xA;","&lt;br/>")'/>
</xsl:template>

<xsl:template name="display-time">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.000')"/>
</xsl:template>

<xsl:template name="display-percent">
    <xsl:param name="value"/>
    <xsl:value-of select="format-number($value,'0.00%')"/>
</xsl:template>

</xsl:stylesheet>
