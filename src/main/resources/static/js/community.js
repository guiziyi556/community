/**
 * Created by uuu on 2020/2/27.
 */
//提交评论的公共方法（针对问题、评论）
function comment2target(targetId, type, commentContent) {
    //评论框内容为空时，前端也进行异常处理
    if (!commentContent) {
        alert("不能回复空内容");
        return;
    }
    /*异步请求*/
    $.ajax({
        type: "POST",
        url: "/comment",
        /*请求类型*/
        contentType: 'application/json',
        /*传入数据*/
        data: JSON.stringify({
            "parentId": targetId,
            "content": commentContent,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
                $("#comment_section").hide();
            } else {
                if (response.code = 2003) {
                    //新建带有确认button的窗口
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        //打开登录新窗口
                        window.open("https://github.com/login/oauth/authorize?client_id=4fa4063b97ae79d7a699&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                }
                else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}
/*
 //展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    //通过toggleClass来完成切换状态
    comments.toggleClass("in");
    //改变点击的状态（二级评论）
    //展开状态
    if (comments.hasClass("in")) {
        //当选中的评论的下拉列表中不为空时，直接加载列表，不需要提交请求
        // var subCommentContainer=$("#comment-"+id);
        /*//////////////////*/
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length >1) {
            e.classList.add("active");
        }
        else {
            e.classList.add("active");
            $.getJSON("/comment/" + id, function (data) {

                //遍历该评论的二级评论列表
                $.each(data.data.reverse(), function (index, comment) {
                    //拼接字段（二级评论列表）
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));
                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));
                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);
                    subCommentContainer.prepend(commentElement);
                });
            });
        }
    } else {
        //折叠状态
        e.classList.remove("active");
    }
}
//给问题进行评论
function post() {
    var questionId = $("#question_id").val();
    var commentContent = $("#comment_content").val();
    comment2target(questionId, 1, commentContent);
}
//给评论进行评论（二级评论）
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);

}

//publish页面的标签选择功能
function  selectTag(e) {
    var value=e.getAttribute("data-tag");
    //获取当前的标签值
    var previous=$("#tag").val();
    if(previous.indexOf(value)==-1){
        if(previous){
            $("#tag").val(previous+','+value);
        }else{
            $("#tag").val(value);
        }
    }
}
//只有当点击标签输入框时，才显示选择标签页
function  showSelectTag() {
    $("#select-tag").show();
}
