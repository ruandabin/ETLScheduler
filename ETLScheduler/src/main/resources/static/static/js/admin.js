layui.define(['jquery', 'form', 'layer', 'element'], function(exports) {
	var $ = layui.jquery,
		form = layui.form,
		layer = layui.layer,
		element = layui.element;
	
	
	
	 element.on('nav(etl_nav)',function(elem){
		//  console.log(elem.children('.layui-nav-child').length); 
		  //console.log(elem); 
//		 
//		  if(elem.context.children.length>0){
//			  return
//		  }else{
//			  alert("sss")
//			  tab.tabAdd(elem.context.lastElementChild.innerText,elem.context.attributes._href.nodeValue,elem[0].offsetParent.id);
//		  }
		 //openTab(elem.context.text,' ',1);
		 tab.tabAdd(elem.context.lastElementChild.innerText,elem.context.attributes._href.nodeValue,elem[0].offsetParent.id);
		 
	 });
	
	
	 var tab = {
		tabAdd:function(title, url, id){
			var li = $("#etl_tab_tip li[lay-id=" + id + "]").length;
			url = url + '?new='+Math.random();
			if(li>0){
				element.tabChange('etl_tab', id);
			}else{
				element.tabAdd('etl_tab',{
					   title: title
					  ,content:'<iframe tab-id="' + id + '" frameborder="0" src="' + url + '" scrolling="auto" style="width: 100%; height: 435px"</iframe>'
					  ,id: id 
				 });
				element.tabChange('etl_tab', id);
			}
		}
	 };
	 
	 

	 
	 
	 exports('admin', {});
	 

});

function logout(){
	 parent.location.href="/logout";
};
