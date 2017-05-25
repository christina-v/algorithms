#ifndef __DFS_CPP__
#define __DFS_CPP__

#include "Graph.hpp"
#include <set>
#include <list>
#include <stack>
#include <string>
#include <iostream>  
using namespace std;
// include more libraries as needed

template <class T>
std::set<T> dfs(Graph<T>& g, T t){

    std::set<T> reachable; //already there
    std::stack<T> stack;    

    Vertex<T> *curV = g.vertices[t];
   
    stack.push(curV->id);
    reachable.insert(curV->id);

    while(!stack.empty()) {
 
    	T id = stack.top();
    	stack.pop();
    
    	Vertex<T>* v = g.vertices[id];
	v->visited = true;
     

     for(auto iterE = v->edges.begin(); iterE != v->edges.end(); iterE++) {
        Vertex<T> * w = g.vertices[*iterE];
	if (!w->visited) {
		w->visited = true;
		stack.push(*iterE);
		reachable.insert(*iterE);
	}
   
     } //end of for loop

    } //end of while



  //curV->visited = true;

    /*reachable.insert(curV->id);   //do I use a pointer her or not?
   
    for(auto it = reachable.begin(); it != reachable.end(); it++) {
	curV = *it;
	curV->visited = true;
       for(auto iterE = curV->edges.begin(); iterE != curV->edges.end(); iterE++) {
 
          Vertex<T> *temp = *iterE;    

          if(*temp->visited == 0) {
            *temp->visited = true;
            reachable.insert(*temp);
            //cout << *temp << " ";
          }
*/

/*
          if(!(*iterE).visited) {
            (*iterE).visited = true;
            reachable.insert((*iterE).id);
            cout << (*iterE).id << " ";
          }

       } //end of inner for loop

    } //end of outter for loop


*/

    // TODO
    return reachable; //already there
}

#endif
