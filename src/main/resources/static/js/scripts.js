$(".answer-write input[type=submit]").click(addAnswer);

function addAnswer(e){
	e.preventDefault();
	console.log("click me");
	
	var queryString = $(".answer-write").serialize();
	console.log("query: " + queryString);
	
	var url = $(".answer-write").attr("action");
	console.log("url :" + url);
	
	$.ajax({ type : 'post',
			url : url,
			data : queryString, 
			dataType : 'json',
			error : onError,
			success : onSuccess	});
			
}

function onError(ex) {
	  var errorData = {
	    name: ex.name, // e.g. ReferenceError
	    message: ex.line, // e.g. x is undefined
	    url: document.location.href,
	    stack: ex.stack // stacktrace string; remember, different per-browser!
	  };

	  $.post('/logger/js/', {
	    data: errorData
	  });
	}

/*function onError() {
	console.log("error:" + data);
}*/

function onSuccess(data, status){
	console.log("data" + data);
	
	var answerTemplate = $("#answerTemplate").html();
	var template = answerTemplate.format(data.writer.userId, data.formattedCreateDate, data.contents, data.question.id, data.id);
	$(".qna-comment-slipp-articles").prepend(template);
	
	$("textarea[name=contents]").val('');
	
	/*var cntTemplate = $("answerCount").html();
	var cnt_template = cntTemplate.format()*/
	

}

$("a.link-delete-article").click(deleteAnswer);

function deleteAnswer(e) {
	e.preventDefault();
	
	var deleteBtn = $(this);
	var url = deleteBtn.attr("href");
	console.log("url :" + url);
	
	$.ajax({
		type : 'delete',
		url : url,
		dataType : 'json',
		error : function( xhr, status){
			console.log("error");
		},
		success : function (data, status) {
			console.log(data);
			if (data.valid) {
				deleteBtn.closest("article").remove();
			} else {
				alert(data.errorMessage);
			}
				
		}
	});
}


String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};