$(function(){
  if ('URLSearchParams' in window) {
    var searchParams = new URLSearchParams(window.location.search);
    let param = searchParams.has('lecture') ? searchParams.get('lecture') == 0 ? '':searchParams.get('lecture') : 0;
    $('#selectLecture').val(param);
  }
});

function onLectureSelection(){
  var lecture = $('#selectLecture').val();
  var url = window.location.protocol+"//"+window.location.host+"/quiz/questions?page=1";
  if(parseInt(lecture) > 0){
    url += "&lecture="+lecture;
  }
  window.location.href = url;
}
