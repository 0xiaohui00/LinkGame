package git.sasure.arrway;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import git.sasure.Abs.SquareArrangement;
import git.sasure.Kit.GameKit;
import git.sasure.linkgame.Piece;

public class randArr extends SquareArrangement 
{

	@Override
	protected List<Piece> createArrangement() 
	{
		List<Piece> notNullPieces = new ArrayList<>();
		Random rand = new Random();
		
		for(int i = 0;i < GameKit.GameXN;++i)
			for(int j = 0;j < GameKit.GameYN;++j)
				if(rand.nextInt() % 2 == 0)
					notNullPieces.add(new Piece(i, j));
		
		return notNullPieces;
	}

}
